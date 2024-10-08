package glueCodeImplementation;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.yaml.snakeyaml.Yaml;
import supportingMethods.CustomerDummyDatabase;
import supportingMethods.SupportingMethods;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class GlueCodeImplementation {

    public static WebDriver driver;
    public SupportingMethods supportingMethods = new SupportingMethods();
    public CustomerDummyDatabase customerDummyDatabase = new CustomerDummyDatabase();
    public static Logger logger = LogManager.getLogger(GlueCodeImplementation.class);

    //To fetch data from Config.yml
    FileInputStream inputStream = new FileInputStream(new File("Config/Config.yml"));
    Yaml yaml = new Yaml();
    Map<String, String> data = yaml.load(inputStream);

    //Constructor
    public GlueCodeImplementation() throws FileNotFoundException {
    }

    public void appOpen() throws FileNotFoundException, InterruptedException {
        driver=supportingMethods.driverSetup();
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get(data.get("AppURL"));
        logger.info("AppURL opened successfully");
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
        logger.info("Login successfully");
        System.out.println("User login to website - Completed");
    }

    @FindBy(xpath ="//a[@href='#']//p[contains(text(),'Customers')]")
    public WebElement HomePageCustomer;
    public void clickCustomerMenu() throws YamlException, FileNotFoundException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.findElement(By.xpath(supportingMethods.readAsObject("HomePage", "Customer"))).click();
        HomePageCustomer.click();
        logger.info("Clicked on customer menu");
        System.out.println("User click on Customer menu - Completed");
    }

    public void clickCustomersOption() throws YamlException, FileNotFoundException {
        driver.findElement(By.xpath(supportingMethods.readAsObject("HomePage", "Customers"))).click();
        logger.info("Clicked on customers option");
        System.out.println("User select Customers option - Completed");
    }

    public void createCustomer() throws YamlException, FileNotFoundException {
        driver.findElement(By.xpath(supportingMethods.readAsObject("HomePage", "AddNew"))).click();
        WebElement Email = driver.findElement(By.id("Email"));
        WebElement Password = driver.findElement(By.id("Password"));
        WebElement FirstName = driver.findElement(By.id("FirstName"));
        WebElement LastName = driver.findElement(By.id("LastName"));

        Faker faker=new Faker();

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String email = fakeValuesService.bothify("????##@gmail.com");
        Email.sendKeys(email);

        Password.sendKeys("root");

        String FakerFirstName=faker.name().firstName();
        FirstName.sendKeys(FakerFirstName);
        customerDummyDatabase.addDummyCustomerDB("FirstName",FakerFirstName);

        LastName.sendKeys(faker.name().lastName());

        driver.findElement(By.id("Gender_Male")).click();

        //Date

        driver.findElement(By.id("Company")).sendKeys(faker.company().name());
        driver.findElement(By.id("IsTaxExempt")).click();
        driver.findElement(By.xpath(supportingMethods.readAsObject("AddNewCustomerPage", "CustomerRoll"))).click();

        //One thing have to select

        new Select(driver.findElement(By.id("VendorId"))).selectByValue("1");

        driver.findElement(By.id("Active")).click();

        driver.findElement(By.id("AdminComment")).sendKeys(faker.gameOfThrones().character());

        driver.findElement(By.xpath(supportingMethods.readAsObject("AddNewCustomerPage", "SaveCustomer"))).click();

        logger.info("Created new Customer");
        System.out.println("User create new customer - Completed");
    }

    public void searchCreatedCustomer() {
        driver.findElement(By.id("SearchFirstName")).sendKeys(customerDummyDatabase.getDummyCustomerDB("FirstName"));
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        driver.findElement(By.id("search-customers")).click();
        logger.info("Searched Created customer");
        System.out.println("User search created customer - Completed");
    }

    public void editSearchedCustomer() throws YamlException, FileNotFoundException {
        try {
            driver.findElement(By.xpath(supportingMethods.readAsObject("EditCustomerPage", "EditButton"))).click();
        }
        catch (Exception e) {
            logger.error("Xpath is not interactable");
            e.printStackTrace();
        }
        WebElement Email = driver.findElement(By.id("Email"));
        Faker faker=new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
        String email = fakeValuesService.bothify("????##@gmail.com");
        Email.clear();
        Email.sendKeys(email);
        System.out.println("Changed Email ID  : " + email);
        customerDummyDatabase.addDummyCustomerDB("Email",email);

        driver.findElement(By.id("Company")).clear();
        driver.findElement(By.id("Company")).sendKeys(faker.company().name());
        logger.info("Edited searched customer");
        System.out.println("User edit searched customer - Completed");
    }

    public void storeCustomerData() throws YamlException, FileNotFoundException {
        supportingMethods.createDbConnection();
        WebElement FirstName = driver.findElement(By.id("FirstName"));
        WebElement LastName = driver.findElement(By.id("LastName"));
        WebElement Email=driver.findElement(By.id("Email"));
        supportingMethods.saveCustomer(FirstName.getAttribute("value"),LastName.getAttribute("value"), Email.getAttribute("value"), "Admin");
        supportingMethods.closeDbConnection();
        driver.findElement(By.xpath(supportingMethods.readAsObject("AddNewCustomerPage", "SaveCustomer"))).click();
        logger.info("Stored data into database");
        System.out.println("User store data into database - Completed");
    }

    public void fetchDbExport() {
        supportingMethods.createDbConnection();
        ResultSet rs= supportingMethods.getCustomerDataFromDatabase();
        supportingMethods.createExcelFile(rs);
        supportingMethods.closeDbConnection();
        logger.info("Fetched DB Export");
        System.out.println("User fetch DB export for the newly created customer - Completed");
    }

    @FindBy(xpath = "//a[normalize-space()='Add new']")
    WebElement Logout;
    public void appLogout() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        driver.findElement(By.partialLinkText("Logo")).click();
        Logout.click();
        logger.info("User Logout");
        System.out.println("User logout from website - Completed");
    }
}
