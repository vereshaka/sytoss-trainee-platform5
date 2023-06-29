package com.sytoss.stp.test.cucumber;

import com.sytoss.stp.test.StpApplicationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@CucumberContextConfiguration
public abstract class StpIntegrationTest<T> extends StpApplicationTest {

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + getPort();
    }

    protected TestExecutionContext<T> getTestExecutionContext() {
        TestExecutionContext<T> testContext = TestExecutionContext.<T>getTestContext();
        if (testContext.getDetails() == null) {
            testContext.setDetails(createDetails());
        }
        return testContext;
    }

    protected abstract T createDetails();
}
