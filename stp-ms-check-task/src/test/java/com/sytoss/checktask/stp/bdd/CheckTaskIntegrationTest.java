package com.sytoss.checktask.stp.bdd;

import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import com.sytoss.stp.test.cucumber.TestExecutionContext;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
public class CheckTaskIntegrationTest extends StpIntegrationTest {

    @Override
    protected TestExecutionContext<CheckTaskDetails> getTestExecutionContext() {
        return super.getTestExecutionContext();
    }
}
