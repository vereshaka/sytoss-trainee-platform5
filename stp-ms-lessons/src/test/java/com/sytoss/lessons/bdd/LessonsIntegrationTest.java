package com.sytoss.lessons.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.lessons.bdd.common.LessonsDetails;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.*;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Random;

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
    private ExamAssigneeConvertor examAssigneeConvertor;

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

    @Autowired
    @MockBean
    private ImageProviderConnector imageProviderConnector;

    @LocalServerPort
    private int applicationPort;

    @Autowired
    private GroupReferenceConnector groupReferenceConnector;

    @Autowired
    private TaskConvertor taskConvertor;

    @Autowired
    private DisciplineConvertor disciplineConvertor;

    @Autowired
    private ExamAssigneeConnector examAssigneeConnector;

    @Autowired
    private ExamAssigneeToConnector examAssigneeToConnector;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestAnalyticsConnector analyticsConnector;

    @Autowired
    private AnalyticsConvertor analyticsConvertor;

    @Override
    protected LessonsDetails createDetails() {
        return new LessonsDetails();
    }

    @Override
    protected String getToken() {
        //TODO: yevgenyv: we should take token from execution context
        return generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "Teacher");
    }

    protected String generateUniqueCode(Long taskDomainId) {
        String code;
        do {
            code = generateCode();
        } while (getTaskConnector().getByCodeAndTaskDomainId(code, taskDomainId) != null);
        return code;
    }

    private String generateCode() {
        Random codeGenerator = new Random();
        int databaseNameLength = 10;
        char letter;
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < databaseNameLength; i++) {
            letter = (char) (codeGenerator.nextInt(26) + 'a');
            name.append(letter);
        }
        return "code-" + name;
    }
}
