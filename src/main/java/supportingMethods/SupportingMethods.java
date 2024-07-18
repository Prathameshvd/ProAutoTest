package supportingMethods;

import org.openqa.selenium.WebDriver;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class SupportingMethods {

    public WebDriver driver;

    FileInputStream inputStream = new FileInputStream(new File("Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    public SupportingMethods() throws FileNotFoundException {
    }

    public WebDriver driverSetup() {
        System.setProperty(data.get("DriverClassName"),data.get("DriverClassPath"));
        return driver;
    }
}
