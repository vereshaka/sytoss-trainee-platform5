package com.sytoss.producer.bdd.when;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PersonalExamWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system create \"(.*)\" personal exam by \"(.*)\" discipline and \"(.*)\" topic with (.*) tasks for student with (.*) id$")
    public void requestSentCreatePersonalExam(String examName, String disciplineName, String topicName, int quantityOfTask, Long studentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("studentId", studentId.toString());
        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setStudentId(studentId);
        examConfiguration.setExamName(examName);
        examConfiguration.setQuantityOfTask(quantityOfTask);
        examConfiguration.setTopics(getTopicId(topicName));
        examConfiguration.setDisciplineId(getDisciplineId(disciplineName));
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(examConfiguration, headers);
        String url = getBaseUrl() + URI + "personalExam/create";
        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }

    @When("the exam with id {word} is done")
    public void theExamIsDoneOnTask(String examId) {
        String url = URI + "personalExam/" + examId + "/summary";

        ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
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

    @When("^student with (.*) id start personal exam \"(.*)\"$")
    public void requestSentStartPersonalExam(String studentId, String personalExamName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("studentId", studentId);
        HttpEntity<Task> requestEntity = new HttpEntity<>(null, headers);
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentId(personalExamName, Long.parseLong(studentId));
        String url = getBaseUrl() + "/api/test/" + input.getId() + "/start";
        log.info("Send request to " + url);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
