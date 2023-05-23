package com.sytoss.checktask.stp.cucumber;

import com.sytoss.checktask.stp.CucumberIntegrationTest;
import io.cucumber.java.en.Given;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GivenStepTest extends CucumberIntegrationTest {

    @Given("database with script: {string}")
    public void givenDatabaseScript(String script) {
        databaseHelperService.get().generateDatabase(script);
    }
}
