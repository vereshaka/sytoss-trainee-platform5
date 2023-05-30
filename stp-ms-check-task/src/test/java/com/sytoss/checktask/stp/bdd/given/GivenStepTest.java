package com.sytoss.checktask.stp.bdd.given;

import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import io.cucumber.java.en.Given;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GivenStepTest extends CucumberIntegrationTest {

    @Given("database with script: {string}")
    public void givenDatabaseScript(String script) {
        TestContext.getInstance().getDatabaseHelperService().get().generateDatabase(script);
    }
}
