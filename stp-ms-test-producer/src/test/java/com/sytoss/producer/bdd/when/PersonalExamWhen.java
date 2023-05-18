package com.sytoss.producer.bdd.when;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.bom.ExamConfiguration;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class PersonalExamWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system create \"(.*)\" personal exam by \"(.*)\" discipline and \"(.*)\" topic with (.*) tasks for student with (.*) id$")
    public void requestSent(String examName, String disciplineName, String topicName, int quantityOfTask, String studentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setStudentId(Long.getLong(studentId));
        examConfiguration.setExamName(examName);
        examConfiguration.setQuantityOfTask(quantityOfTask);
        examConfiguration.setTopics(getTopicId(topicName));
        examConfiguration.setDisciplineId(getDisciplineId(disciplineName));
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(examConfiguration, headers);
        String url = getBaseUrl() + URI + "personalExam/create";
        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }

    private List<Long> getTopicId(String name) {
        List<Long> topicsId = new ArrayList<>();
        if ("Join".equals(name)) {
            topicsId.add(1L);
        } else if ("SELECT".equals(name)) {
            topicsId.add(2L);
        } else {
            topicsId.add(3L);
        }
        return topicsId;
    }

    private Long getDisciplineId(String name) {
        if ("SQL".equals(name)) {
            return 1L;
        } else if ("ORACLE".equals(name)) {
            return 2L;
        } else {
            return 3L;
        }
    }
}
