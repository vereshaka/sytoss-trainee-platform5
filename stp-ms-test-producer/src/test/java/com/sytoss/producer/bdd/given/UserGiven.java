package com.sytoss.producer.bdd.given;

import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class UserGiven extends TestProducerIntegrationTest {

    @Given("^(student|teacher) \"(.*)\" \"(.*)\" with \"(.*)\" email exists$")
    public void teacherExists(String userType, String firstName, String lastName, String email) {
        Long userId = 123L;
        String userUid ="111-222-333";
        getTestExecutionContext().getDetails().setStudentId(userId);
        getTestExecutionContext().getDetails().setStudentUid(userUid);
        getTestExecutionContext().setToken(generateJWT(new ArrayList<>(), firstName, lastName, email, userType));
        LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("id", getTestExecutionContext().getDetails().getStudentId());
        userMap.put("sub", getTestExecutionContext().getDetails().getStudentUid());
        Map<String, Object> primaryGroup = new HashMap<>();
        primaryGroup.put("id", 123);
        userMap.put("primaryGroup", primaryGroup);
        when(getUserConnector().getMyProfile()).thenReturn(userMap);
    }
}
