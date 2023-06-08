package com.sytoss.lessons.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
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

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")

@SelectClasspathResource("/features")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @Bug and not @Skip")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sytoss.lessons.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html")

@CucumberContextConfiguration
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Getter
public class CucumberIntegrationTest extends AbstractApplicationTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private TopicConnector topicConnector;

    @Autowired
    private TaskConnector taskConnector;

    @Autowired
    private TaskDomainConnector taskDomainConnector;

    @Autowired
    private DisciplineConnector disciplineConnector;

    @Autowired
    private ExamConnector examConnector;

    @Autowired
    private TopicConvertor topicConvertor;

    @Autowired
    private GroupConvertor groupConvertor;

    @Autowired
    private TaskConditionConnector taskConditionConnector;

    @LocalServerPort
    private int applicationPort;

    @Autowired
    private GroupConnector groupConnector;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }
}