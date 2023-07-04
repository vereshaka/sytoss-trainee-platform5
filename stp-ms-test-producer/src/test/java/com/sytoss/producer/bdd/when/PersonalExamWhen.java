package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
public class PersonalExamWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system create \"(.*)\" personal exam by \"(.*)\" discipline and \"(.*)\" topic with (.*) tasks for student with (.*) id$")
    public void requestSentCreatePersonalExam(String examName, String disciplineName, String topicName, int quantityOfTask, String studentId) throws JOSEException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(generateJWT(List.of("123")));
        when(getImageConnector().convertImage(anyString())).thenReturn(1L);
        ExamConfiguration examConfiguration = new ExamConfiguration();
        Student student = new Student();
        student.setUid(studentId);
        examConfiguration.setStudent(student);
        examConfiguration.setExamName(examName);
        examConfiguration.setQuantityOfTask(quantityOfTask);
        examConfiguration.setTopics(getTopicId(topicName));
        examConfiguration.setDisciplineId(getDisciplineId(disciplineName));
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(examConfiguration, headers);
        String url = getBaseUrl() + URI + "personalExam/create";
        ResponseEntity<PersonalExam> responseEntity = getRestTemplate().postForEntity(url, requestEntity, PersonalExam.class);
        IntegrationTest.getTestContext().setPersonalExamResponse(responseEntity);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("the exam with id {word} is done")
    public void theExamIsDoneOnTask(String examId) throws JOSEException {
        String url = URI + "personalExam/" + examId + "/summary";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<PersonalExam> responseEntity = doGet(url, requestEntity, PersonalExam.class);
        IntegrationTest.getTestContext().setPersonalExamResponse(responseEntity);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
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
    public void startPersonalExam(String studentId, String personalExamName) throws JOSEException {
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentUid(personalExamName, studentId);
        String url = getBaseUrl() + "/api/test/" + input.getId() + "/start";
        log.info("Send request to " + url);
        HttpEntity<Task> requestEntity = startTest(studentId);
        ResponseEntity<Question> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Question.class);
        IntegrationTest.getTestContext().setFirstTaskResponse(responseEntity);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^student with (.*) id start second time personal exam \"(.*)\"$")
    public void startSecondTimePersonalExam(String studentId, String personalExamName) throws JOSEException {
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentUid(personalExamName, studentId);
        String url = getBaseUrl() + "/api/test/" + input.getId() + "/start";
        log.info("Send request to " + url);
        HttpEntity<Task> requestEntity = startTest(studentId);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^system check task domain with id: \"(.*)\" is used$")
    public void systemCheckTaskDomain(String taskDomainId) throws JOSEException {
        String url = "/api/taskDomain/" + Long.parseLong(taskDomainId) + "/isUsedNow";
        log.info("Send request to " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
    }

    private HttpEntity startTest(String studentId) throws JOSEException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(generateJWT(List.of("123"), studentId));
        HttpEntity<Task> requestEntity = new HttpEntity<>(null, headers);
        return requestEntity;
    }
}
