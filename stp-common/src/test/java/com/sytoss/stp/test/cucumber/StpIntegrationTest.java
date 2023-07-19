package com.sytoss.stp.test.cucumber;

import com.sytoss.stp.test.StpApplicationTest;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
public abstract class StpIntegrationTest<T> extends StpApplicationTest {

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + getPort();
    }

    protected TestExecutionContext<T> getTestExecutionContext() {
        TestExecutionContext<T> testContext = TestExecutionContext.getTestContext();
        if (testContext.getDetails() == null) {
            testContext.setDetails(createDetails());
        }
        return testContext;
    }

    protected abstract T createDetails();

    @Override
    protected String getToken() {
        return getTestExecutionContext().getToken();
    }
}
