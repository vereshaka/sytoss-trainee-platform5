package com.sytoss.checktask.stp.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.checktask.stp.AbstractApplicationTest;
import com.sytoss.checktask.stp.bdd.other.CheckTaskDetails;
import com.sytoss.stp.test.StpApplicationTest;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
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
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
public class CheckTaskIntegrationTest extends StpIntegrationTest<CheckTaskDetails> {

    @Override
    protected CheckTaskDetails createDetails() {
        return new CheckTaskDetails();
    }
}
