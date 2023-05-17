package com.sytoss.producer.bdd;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@CucumberContextConfiguration
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.csm.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html")
//@SpringJUnitConfig(classes = CSMConfiguration.class, initializers = {ConfigDataApplicationContextInitializer.class})
@Getter
public class CucumberIntegrationTest extends AbstractSTPProducerApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int applicationPort;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }
}
