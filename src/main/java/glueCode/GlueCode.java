package glueCode;

import glueCodeImplementation.GlueCodeImplementation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.FileNotFoundException;

public class GlueCode {
    GlueCodeImplementation glueCodeImplementation=new GlueCodeImplementation();

    @Given("user navigates to the website nopCommerce")
    public void user_navigates_to_the_website_nop_commerce() throws FileNotFoundException {
        glueCodeImplementation.appLogin();
    }
    @Given("user login to website")
    public void user_login_to_website() {
        System.out.println("user login to website");
    }
    @Given("user click on Customers menu")
    public void user_click_on_customers_menu() {
        System.out.println("user click on Customers menu");
    }
    @Given("user select Customer option")
    public void user_select_customer_option() {
        System.out.println("user select Customer option");
    }
    @Given("user create new customer")
    public void user_create_new_customer() {
        System.out.println("user create new customer");
    }
    @Given("user search created customer")
    public void user_search_created_customer() {
        System.out.println("user search created customer");
    }
    @Given("user edit searched customer")
    public void user_edit_searched_customer() {
        System.out.println("user edit searched customer");
    }
    @Given("user store data into database")
    public void user_store_data_into_database() {
        System.out.println("user store data into database");
    }
    @Given("user fetch DB export for the newly created customer")
    public void user_fetch_db_export_for_the_newly_created_customer() {
        System.out.println("user fetch DB export for the newly created customer");
    }
    @Given("user logout from website")
    public void user_logout_from_website() {
        System.out.println("user logout from website");
    }

}