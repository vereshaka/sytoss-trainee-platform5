package com.sytoss.checktask.stp.cucumber;

import io.cucumber.java.en.Given;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GivenStepTest {

    @Given("database with script: {string}")
    public void givenDatabaseScript(String script) {
        TestContext.getInstance().getDatabaseHelperService().get().generateDatabase(script);
    }
}
