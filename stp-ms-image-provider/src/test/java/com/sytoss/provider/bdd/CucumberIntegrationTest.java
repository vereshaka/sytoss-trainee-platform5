package com.sytoss.provider.bdd;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import com.sytoss.stp.test.cucumber.TestExecutionContext;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
@Getter
@CucumberContextConfiguration
public class CucumberIntegrationTest extends StpIntegrationTest {

    @Override
    protected TestExecutionContext<?> getTestExecutionContext() {
        return super.getTestExecutionContext();
    }

    @Override
    protected Object createDetails() {
        return null;
    }

    @Autowired
    private ImageConnector imageConnector;
}