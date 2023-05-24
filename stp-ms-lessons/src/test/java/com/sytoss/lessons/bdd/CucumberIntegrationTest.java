package com.sytoss.lessons.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.lessons.commonConnectors.DisciplineConnectorTest;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.controllers.AbstractControllerTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@CucumberContextConfiguration
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.lessons.bdd")
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

    @Autowired
    private GroupConnector groupConnector;

    @Autowired
    private DisciplineConnectorTest disciplineConnector;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }
}
