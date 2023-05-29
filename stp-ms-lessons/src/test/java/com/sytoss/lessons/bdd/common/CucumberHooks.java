package com.sytoss.lessons.bdd.common;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;

public class CucumberHooks extends CucumberIntegrationTest {

    @After
    public void tearDown() {
        TestExecutionContext.drop();
    }
}
