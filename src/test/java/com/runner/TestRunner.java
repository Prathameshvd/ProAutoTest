package com.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "feature/Sample.feature"
        ,tags = "@Create_save_edit_and_save_Customer"
        ,glue={"glueCode", "supportingMethods"}
        ,monochrome = false
)

public class TestRunner {

}
