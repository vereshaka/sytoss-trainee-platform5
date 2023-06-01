package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain with \"(.*)\" id exist$")
    public void taskDomainExists(String taskDomainName, Integer taskDomainId) {

    }
}
