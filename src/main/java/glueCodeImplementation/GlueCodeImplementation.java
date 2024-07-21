package glueCodeImplementation;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.yaml.snakeyaml.Yaml;
import supportingMethods.SupportingMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Map;

public class GlueCodeImplementation {

    public WebDriver driver;
    public SupportingMethods supportingMethods;

    FileInputStream inputStream = new FileInputStream(new File("Config/Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    public GlueCodeImplementation() throws FileNotFoundException {
    }

    public void appOpen() throws FileNotFoundException {
        supportingMethods=new SupportingMethods();
        driver=supportingMethods.driverSetup();
        driver.manage().window().maximize();
        driver.get(data.get("AppURL"));
        System.out.println("User navigates to the website nopCommerce - Completed");
    }

    public void appLogin()
    {
        WebElement UserName = driver.findElement(By.id("Email"));
        UserName.clear();
        UserName.sendKeys(data.get("UserName"));

        WebElement Password = driver.findElement(By.id("Password"));
        Password.clear();
        Password.sendKeys(data.get("Password"));

        driver.findElement(By.className("button-1")).click();

        System.out.println("User login to website - Completed");
    }

    public void clickCustomerMenu() throws YamlException, FileNotFoundException {
        driver.findElement(By.xpath(supportingMethods.readAsObject("Cust"))).click();
        System.out.println("User click on Customer menu - Completed");
    }

    public void clickCustomersOption() {
        driver.findElement(By.xpath("//a[@href='/Admin/Customer/List']//p[contains(text(),'Customers')]")).click();
        System.out.println("User select Customers option - Completed");
    }

    public void createCustomer() {
        driver.findElement(By.xpath("//a[normalize-space()='Add new']")).click();
        WebElement Email = driver.findElement(By.id("Email"));
        WebElement Password = driver.findElement(By.id("Password"));
        WebElement FirstName = driver.findElement(By.id("FirstName"));
        WebElement LastName = driver.findElement(By.id("LastName"));

        Faker faker=new Faker();

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String email = fakeValuesService.bothify("????##@gmail.com");
        System.out.println("email");
        Email.sendKeys(email);

        Password.sendKeys("root");
        FirstName.sendKeys(faker.name().firstName());
        LastName.sendKeys(faker.name().lastName());

        driver.findElement(By.id("Gender_Male")).click();

        //Date


        driver.findElement(By.id("Company")).sendKeys(faker.company().name());
        driver.findElement(By.id("IsTaxExempt")).click();
        driver.findElement(By.xpath("(//input[@role='searchbox'])[1]")).click();

        //One thing have to select

        new Select(driver.findElement(By.id("VendorId"))).selectByValue("1");
        driver.findElement(By.id("AdminComment")).sendKeys(faker.gameOfThrones().character());

        System.out.println("User create new customer - Completed");
    }

    public void appLogout() {
        driver.findElement(By.partialLinkText("Logo")).click();
        driver.quit();
        System.out.println("User logout from website - Completed");
    }
}
