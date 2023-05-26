package com.sytoss.producer.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.producer.controllers.AbstractControllerTest;
import com.sytoss.producer.commonConnectors.PersonalExamConnectorTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@CucumberContextConfiguration
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.producer.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html")
@ExtendWith(SpringExtension.class)
@Getter
public class CucumberIntegrationTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private int applicationPort;

    @MockBean
    @Autowired
    private CheckTaskConnector checkTaskConnector;

    @Autowired
    private PersonalExamConnectorTest personalExamConnector;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }
}
