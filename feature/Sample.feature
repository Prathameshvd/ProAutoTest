Feature: Login

@Create_save_edit_and_save_Customer
Scenario: To create, search, edit and save customer.
Given user navigates to the website nopCommerce
And user login to website
And user click on Customer menu
And user select Customers option
And user create new customer
And user search created customer
And user edit searched customer
And user take screenshot for newly created customer
And user store data into database
And user fetch DB export for the newly created customer
And user logout from website