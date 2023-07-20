package com.sytoss.lessons.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.lessons.bdd.common.LessonsDetails;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Getter
@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
public class LessonsIntegrationTest extends StpIntegrationTest<LessonsDetails> {

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

    @Autowired
    @MockBean
    private CheckTaskConnector checkTaskConnector;

    @LocalServerPort
    private int applicationPort;

    @Autowired
    private GroupReferenceConnector groupReferenceConnector;

    @Autowired
    private TaskConvertor taskConvertor;

    @Override
    protected LessonsDetails createDetails() {
        return new LessonsDetails();
    }
}
