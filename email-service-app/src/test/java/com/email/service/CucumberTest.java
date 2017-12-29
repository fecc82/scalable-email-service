package com.email.service;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features"}, plugin = {
        "pretty",
        "html:target/site/cucumber-pretty",
        "json:target/cucumber.json",
        "junit:target/failsafe-reports/cucumber-junit.xml",
}, monochrome = true, strict = true, glue = {
        ""
})
public class CucumberTest {
}
