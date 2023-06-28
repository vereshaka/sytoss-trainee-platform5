package com.sytoss.users.bdd;

import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")

@SelectClasspathResource("/features")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @Bug and not @Skip")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.users.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html")

@CucumberContextConfiguration
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Getter
public class CucumberIntegrationTest extends AbstractApplicationTest {

    @Autowired
    private UserConnector userConnector;

    @Autowired
    private GroupConnector groupConnector;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private GroupConvertor groupConvertor;

    @LocalServerPort
    private int applicationPort;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }

    protected String getToken() {
        UserDTO user = TestExecutionContext.getTestContext().getUser();
        String userType = user instanceof StudentDTO ? "student" : "teacher";
        return generateJWT(new ArrayList<>(), user.getFirstName(), user.getLastName(), user.getEmail(), userType);
    }
}
