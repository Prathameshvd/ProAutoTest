package supportingMethods;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class SupportingMethods {

    public WebDriver driver;

    //To load data from the YAML file
    FileInputStream inputStream = new FileInputStream(new File("Config/Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    //To handle FileNotFoundException excepyion
    public SupportingMethods() throws FileNotFoundException {
    }

    //To setup driver
    public WebDriver driverSetup() {
        System.setProperty(data.get("DriverClassName"),data.get("DriverClassPath"));
        driver=new ChromeDriver();
        return driver;
    }
}
