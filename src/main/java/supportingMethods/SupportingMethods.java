package supportingMethods;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportingMethods {
    public CustomerDummyDatabase customerDummyDatabase = new CustomerDummyDatabase();
    public WebDriver driver;
    public Connection con;

    //To load data from the Config YAML file
    FileInputStream inputStream = new FileInputStream(new File("Config/Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    //To handle FileNotFoundException exception
    public SupportingMethods() throws FileNotFoundException {

    }

    //To setup driver
    public WebDriver driverSetup() {
        if(data.get("Browser").equalsIgnoreCase("Chrome")) {
            System.setProperty(data.get("ChromeDriverClassName"), data.get("ChromeDriverClassPath"));
            driver = new ChromeDriver();
        }
        else{
            System.setProperty(data.get("EdgeDriverClassName"),data.get("EdgeDriverClassPath"));
            driver=new EdgeDriver();
        }
        return driver;
    }

    //To get Xpath from Xpath.yml file
    public String readAsObject(String Page, String Value) throws FileNotFoundException, YamlException {
        YamlReader yamlReader = new YamlReader(new FileReader("InputData/Xpath.yml"));
        Map read = (Map) yamlReader.read();
        Map<String, String> sub = (Map<String, String>) read.get(Page);
        return (String) sub.get(Value);
    }

    //To Db Connection
    public void createDbConnection() {
        try {
            con= DriverManager.getConnection(data.get("DBurl"), data.get("DBUserName"), data.get("DBPassword"));
            String temp = con==null ? "JDBC Connection unsuccessful !" : "JDBC Connection Successfully !";
            System.out.println(temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To close Db Connection
    public void closeDbConnection() {
        try {
            con.close();
            System.out.println("JDBC Connection Closed Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To save data into Database
    public void saveCustomer(String FirstName, String LastName, String Email, String Comment) {
        try {
            PreparedStatement preparedStatement4 = con.prepareStatement("Insert into nopcommerce (First_Name, Last_Name, Email, Additional_Details) Values (?,?,?,?)");
            preparedStatement4.setString(1, FirstName);
            preparedStatement4.setString(2, LastName);
            preparedStatement4.setString(3, Email);
            preparedStatement4.setString(4, Comment);
            preparedStatement4.execute();
            System.out.println("Customer details stored successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //To get Data from Database
    public ResultSet getCustomerDataFromDatabase() {
        try {
            PreparedStatement preparedStatement= con.prepareStatement("Select * from nopcommerce where First_Name=?");
            preparedStatement.setString(1,customerDummyDatabase.getDummyCustomerDB("FirstName"));
            ResultSet rs= preparedStatement.executeQuery();
            rs.next();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //To create excel report
    public void createExcelFile(ResultSet rs) {
        XSSFWorkbook workbook=new XSSFWorkbook();
        XSSFSheet sheet= workbook.createSheet("NewCustomer");
        try {
            ResultSetMetaData resultSetMetaData= rs.getMetaData();
            int ColumnCount = resultSetMetaData.getColumnCount();
            List<Object> Header=new ArrayList<>();
            for(int i=1; i<=ColumnCount; i++)
            {
                Header.add(resultSetMetaData.getColumnName(i));
            }
            XSSFRow HeaderRow = sheet.createRow(0);
            for(int i=0; i<ColumnCount;i++) {
                XSSFCell cell = HeaderRow.createCell(i);
                cell.setCellValue(String.valueOf(Header.get(i)));
            }
            XSSFRow row;
            XSSFCell cell;
            int RowCount=1;
            do {
                row= sheet.createRow(RowCount++);
                    int CellCount=1;
                for(int i=0; i<ColumnCount;i++) {
                    cell = row.createCell(i);
                    System.out.print("  " + rs.getObject(CellCount));
                    cell.setCellValue(String.valueOf(rs.getObject(CellCount)));
                    CellCount++;
                }
            }while(rs.next());
            FileOutputStream fileOutputStream=new FileOutputStream(new File("D:/LightWaitSW/IntelliJ IDEA/IdeaProjects/CucumberBasedProject/src/main/resources/Customer.xlsx"));
            workbook.write(fileOutputStream);
            workbook.close();
            System.out.println();
            System.out.println("Data stored into excel file successfully");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}