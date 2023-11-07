package com.sytoss.producer.controllers;

import com.sytoss.producer.connectors.UserConnector;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.mockito.Mockito.when;

public class TestProducerControllerTest extends StpApplicationTest {

    @InjectMocks
    protected PersonalExamController personalExamController;

    @MockBean
    protected PersonalExamService personalExamService;

    @MockBean
    protected AnswerService answerService;

    @MockBean
    protected UserConnector userConnector;

    @BeforeEach
    public void mockGetMyProfile() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Johnson");
        user.put("email", "test@test.com");
        when(userConnector.getMyProfile()).thenReturn(user);
    }

    @Override
    protected String getToken() {
        return generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "Teacher");
    }
}
