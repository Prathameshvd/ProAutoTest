package supportingMethods;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SupportingMethods {

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
        System.setProperty(data.get("DriverClassName"),data.get("DriverClassPath"));
        driver=new ChromeDriver();
        return driver;
    }

    //To select Data
    public void selectDate(String SelectedDate)
    {

    }

    //Under development
    public String readAsObject(String value) throws FileNotFoundException, YamlException {
        YamlReader yamlReader = new YamlReader(new FileReader("InputData/Xpath.yml"));
        Map read = (Map) yamlReader.read();

        //This is still under development
        System.out.println(read.entrySet());
        System.out.println(read);
        return (String) read.get(value);
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
}

