package com.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "feature/Sample.feature"
        ,glue={"glueCode"}
        ,monochrome = false
)

public class TestRunner {

}
