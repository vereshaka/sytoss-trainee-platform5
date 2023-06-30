package com.sytoss.checktask.stp.bdd.other;

import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;

public class STPHooks extends CucumberIntegrationTest {

    @After
    public void tearDown() {
        TestContext.drop();
    }
}