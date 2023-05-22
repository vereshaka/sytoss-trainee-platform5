package com.sytoss.stp;

import com.sytoss.stp.service.DatabaseHelperService;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static org.mockito.Mockito.mock;

@Suite
@IncludeEngines("cucumber")
@CucumberContextConfiguration
@SelectClasspathResource("features")
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class})
@CucumberOptions(features = "src/test/resources/features", glue = "com.sytoss.stp", tags = "not @SingleRun")
public class CucumberIntegrationTest {

    protected final DatabaseHelperService databaseHelperService = mock(DatabaseHelperService.class);

}
