package supportingMethods;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class SupportingMethods {
    public CustomerDummyDatabase customerDummyDatabase = new CustomerDummyDatabase();
    public WebDriver driver;
    public Connection con;
    public Logger logger = LogManager.getLogger(this.getClass());

    //To load data from the Config YAML file
    FileInputStream inputStream = new FileInputStream(new File("Config/Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    //To handle FileNotFoundException exception
    public SupportingMethods() throws FileNotFoundException {
    }

    //To set up driver
    public WebDriver driverSetup() {
        if((data.get("Browser").equalsIgnoreCase("Chrome")) && (data.get("Grid").equalsIgnoreCase("NoGrid"))) {
            ChromeOptions chromeOptions =new ChromeOptions();
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.setExperimentalOption("excludeSwitches",new String[]{"enable-automation"});
            chromeOptions.addArguments("--incognito");
            System.setProperty(data.get("ChromeDriverClassName"), data.get("ChromeDriverClassPath"));
            driver = new ChromeDriver(chromeOptions);
        }

        if (data.get("Browser").equalsIgnoreCase("Edge") && (data.get("Grid").equalsIgnoreCase("NoGrid"))){
            System.setProperty(data.get("EdgeDriverClassName"),data.get("EdgeDriverClassPath"));
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.setExperimentalOption("useAutomationExtension","false");
            edgeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver=new EdgeDriver(edgeOptions);
        }

        if(data.get("Browser").equalsIgnoreCase("Chrome") && data.get("Grid").equalsIgnoreCase("Grid")) {
            System.setProperty(data.get("ChromeDriverClassName"), data.get("ChromeDriverClassPath"));
            try {
                URL GridUrl = new URL(data.get("GridURL"));
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setBrowserName("chrome");

                ChromeOptions chromeOptions =new ChromeOptions();
                desiredCapabilities.merge(chromeOptions);
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setExperimentalOption("excludeSwitches",new String[]{"enable-automation"});
                driver = new RemoteWebDriver(GridUrl,desiredCapabilities);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (data.get("Browser").equalsIgnoreCase("Edge") && data.get("Grid").equalsIgnoreCase("Yes")){
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
            if (con==null)
                logger.error("JDBC Connection unsuccessful !");
            else
                logger.info("JDBC Connection Successfully !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To close Db Connection
    public void closeDbConnection() {
        try {
            con.close();
            System.out.println("JDBC Connection Closed Successfully");
            logger.info("JDBC Connection Closed Successfully");
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
            logger.info("Data inserted into database successfully");
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
            logger.info("Data fetched from database successfully");
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
            logger.info("Data stored into excel successfully");
            System.out.println("Data stored into excel file successfully");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}