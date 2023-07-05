package com.sytoss.producer.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.producer.AbstractApplicationTest;
import com.sytoss.producer.common.connectors.PersonalExamConnectorTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.ImageConnector;
import com.sytoss.producer.connectors.MetadataConnector;
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
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@CucumberContextConfiguration
@SelectClasspathResource("features")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @Bug and not @Skip")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.producer.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html")
@ExtendWith(SpringExtension.class)
@Getter
public class CucumberIntegrationTest extends AbstractApplicationTest {

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

    @Autowired
    @MockBean
    private ImageConnector imageConnector;

    @MockBean
    @Autowired
    private MetadataConnector metadataConnector;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }
}
