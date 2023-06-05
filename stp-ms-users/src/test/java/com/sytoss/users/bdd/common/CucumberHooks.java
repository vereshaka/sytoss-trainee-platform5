package com.sytoss.users.bdd.common;


import com.sytoss.users.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;

public class CucumberHooks extends CucumberIntegrationTest {

    @After
    public void tearDown() {
        TestExecutionContext.drop();
    }
}
