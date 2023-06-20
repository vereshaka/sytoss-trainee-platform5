package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.en.Then;

public class ConditionThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" condition with (.*) type should be in task with question \"(.*)\"$")
    public void conditionShouldExistInTask(String conditionName, ConditionType type, String question) {
    }
}