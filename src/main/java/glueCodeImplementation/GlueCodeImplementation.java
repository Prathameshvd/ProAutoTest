package glueCodeImplementation;

import org.openqa.selenium.WebDriver;
import supportingMethods.SupportingMethods;

import java.io.FileNotFoundException;

public class GlueCodeImplementation {

    public WebDriver driver;
    public SupportingMethods supportingMethods;

    public void appLogin() throws FileNotFoundException {
        supportingMethods=new SupportingMethods();
        driver=supportingMethods.driverSetup();
        System.out.println("user navigates to the website nopCommerce");
    }


}
