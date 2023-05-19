package com.sytoss.producer.bdd.common;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;

public class STPHooks extends CucumberIntegrationTest {

    @After
    public void tearDown() {
        IntegrationTest.drop();
    }
}
