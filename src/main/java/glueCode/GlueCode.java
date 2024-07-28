package glueCode;

import com.esotericsoftware.yamlbeans.YamlException;
import glueCodeImplementation.GlueCodeImplementation;
import io.cucumber.java.en.Given;

import java.io.FileNotFoundException;

public class GlueCode {
    GlueCodeImplementation glueCodeImplementation=new GlueCodeImplementation();

    public GlueCode() throws FileNotFoundException {
    }

    @Given("user navigates to the website nopCommerce")
    public void user_navigates_to_the_website_nop_commerce() throws FileNotFoundException {
        glueCodeImplementation.appOpen();
    }
    @Given("user login to website")
    public void user_login_to_website() {
        glueCodeImplementation.appLogin();
    }

    @Given("user click on Customer menu")
    public void user_click_on_customers_menu() throws YamlException, FileNotFoundException {
        glueCodeImplementation.clickCustomerMenu();
    }

    @Given("user select Customers option")
    public void user_select_customer_option() {
        glueCodeImplementation.clickCustomersOption();
    }

    @Given("user create new customer")
    public void user_create_new_customer() {
        glueCodeImplementation.createCustomer();
    }

    @Given("user search created customer")
    public void user_search_created_customer() {
        glueCodeImplementation.searchCreatedCustomer();
    }

    @Given("user edit searched customer")
    public void user_edit_searched_customer() {
        glueCodeImplementation.editSearchedCustomer();
    }

    @Given("user take screenshot for newly created customer")
    public void user_take_screenshot_for_newly_created_customer() {
        glueCodeImplementation.takeScreenshotForCreatedCustomer();
    }

    @Given("user store data into database")
    public void user_store_data_into_database() {
        glueCodeImplementation.storeCustomerData();
    }

    @Given("user fetch DB export for the newly created customer")
    public void user_fetch_db_export_for_the_newly_created_customer() {
        glueCodeImplementation.fetchDbExport();
    }

    @Given("user logout from website")
    public void user_logout_from_website() {
        glueCodeImplementation.appLogout();
    }

}