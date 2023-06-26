package com.sytoss.lessons.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@CucumberContextConfiguration
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Getter
@Deprecated
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
    private TaskConditionConnector taskConditionConnector;

    @Autowired
    @MockBean
    private UserConnector userConnector;

    @Autowired
    private TaskDomainConvertor taskDomainConvertor;

    @Autowired
    @MockBean
    private PersonalExamConnector personalExamConnector;

    @LocalServerPort
    private int applicationPort;

    @Autowired
    private GroupReferenceConnector groupReferenceConnector;

    protected String getBaseUrl() {
        return "http://127.0.0.1:" + applicationPort;
    }

    protected String getToken() {
        return TestExecutionContext.getTestContext().getToken();
    }
}
