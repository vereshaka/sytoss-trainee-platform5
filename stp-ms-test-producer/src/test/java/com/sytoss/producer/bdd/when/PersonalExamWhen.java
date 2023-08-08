package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
public class PersonalExamWhen extends TestProducerIntegrationTest {

    private final String URI = "/api/";

    @When("^system create \"(.*)\" personal exam by \"(.*)\" discipline and \"(.*)\" topic with (.*) tasks for student with (.*) id$")
    public void requestSentCreatePersonalExam(String examName, String disciplineName, String topicName, int quantityOfTask, String studentId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        when(getImageConnector().convertImage(anyString())).thenReturn(1L);
        ExamConfiguration examConfiguration = new ExamConfiguration();
        Student student = new Student();
        student.setUid(studentId);
        examConfiguration.setStudent(student);
        examConfiguration.setExamName(examName);
        examConfiguration.setQuantityOfTask(quantityOfTask);
        examConfiguration.setTopics(getTopicId(topicName));
        examConfiguration.setDisciplineId(getDisciplineId(disciplineName));

        String[] tasks = getTestExecutionContext().getDetails().getTaskMapping().get(topicName).split(", ");
        List<Task> taskList = new ArrayList<>();
        for (String task : tasks) {
            Task newTask = new Task();
            newTask.setQuestion(task);
            taskList.add(newTask);
        }

        Discipline discipline = new Discipline();
        discipline.setId(getDisciplineId(disciplineName));
        discipline.setName(disciplineName);
        when(getMetadataConnector().getDiscipline(anyLong())).thenReturn(discipline);
        when(getMetadataConnector().getTasksForTopic(anyLong())).thenReturn(taskList);
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(examConfiguration, httpHeaders);
        String url = getBaseUrl() + URI + "personal-exam/create";
        ResponseEntity<PersonalExam> responseEntity = getRestTemplate().postForEntity(url, requestEntity, PersonalExam.class);
        getTestExecutionContext().getDetails().setPersonalExamResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^the exam with id (.*) is done$")
    public void theExamIsDoneOnTask(String examId) {
        String url = URI + "personal-exam/" + examId + "/summary";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> responseEntity = doGet(url, requestEntity, PersonalExam.class);
        getTestExecutionContext().getDetails().setPersonalExamResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
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
        String url = getBaseUrl() + "/api/personal-exam/" + input.getId() + "/start";
        log.info("Send request to " + url);
        HttpEntity<Task> requestEntity = startTest(studentId);
        ResponseEntity<Question> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Question.class);
        getTestExecutionContext().getDetails().setFirstTaskResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^student with (.*) id start second time personal exam \"(.*)\"$")
    public void startSecondTimePersonalExam(String studentId, String personalExamName) throws JOSEException {
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentUid(personalExamName, studentId);
        String url = getBaseUrl() + "/api/personal-exam/" + input.getId() + "/start";
        log.info("Send request to " + url);
        HttpEntity<Task> requestEntity = startTest(studentId);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        getTestExecutionContext().getDetails().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^system check task domain with id: \"(.*)\" is used$")
    public void systemCheckTaskDomain(String taskDomainId) {
        String url = "/api/personal-exam/is-used-now/task-domain/" + Long.parseLong(taskDomainId);
        log.info("Send request to " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
        getTestExecutionContext().getDetails().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    private HttpEntity<Task> startTest(String studentId) throws JOSEException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentId));
        return new HttpEntity<>(null, httpHeaders);
    }

    @When("operation for retrieving personal exams for examId {} was called")
    public void retrievePersonalExamsByExamId(Long examId) {
        String url = "/api/exam/" + examId + "/personal-exams";
        log.info("Send request to " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
        getTestExecutionContext().getDetails().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("operation for retrieving personal exams for userId {} was called")
    public void retrievePersonalExamsByUserId(Long userId) {
        String url = "/api/personal-exam/user/" + userId;
        log.info("Send request to " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
        getTestExecutionContext().getDetails().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }
}
