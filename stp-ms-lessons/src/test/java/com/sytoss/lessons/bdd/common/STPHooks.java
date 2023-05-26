package com.sytoss.lessons.bdd.common;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;

public class STPHooks extends CucumberIntegrationTest {

    @After
    public void tearDown() {
        IntegrationTest.drop();
    }
}
