package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
public class PersonalExamWhen extends TestProducerIntegrationTest {

    private final String URI = "/api/";

    @When("^system create \"(.*)\" personal exam with maxGrade (.*) by \"(.*)\" discipline and \"(.*)\" topic with (.*) tasks for student with (.*) id between (.*) and (.*)$")
    public void requestSentCreatePersonalExam(String examName, Integer maxGrade, String disciplineName, String topicName, int numberOfTasks,
                                              String studentId, String relevantFrom, String relevantTo) throws ParseException {
        String[] tasks = getTestExecutionContext().getDetails().getTaskMapping().get(topicName).split(", ");
        List<Task> taskList = new ArrayList<>();
        for (String task : tasks) {
            Task newTask = new Task();
            newTask.setCoef(1d);
            newTask.setQuestion(task);
            taskList.add(newTask);
        }

        ExamConfiguration examConfiguration = new ExamConfiguration();
        Student student = new Student();
        student.setUid(studentId);
        examConfiguration.setStudent(student);

        examConfiguration.setExamAssignee(new ExamAssignee());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        examConfiguration.getExamAssignee().setRelevantFrom(dateFormat.parse(relevantFrom));
        examConfiguration.getExamAssignee().setRelevantTo(dateFormat.parse(relevantTo));

        examConfiguration.setExam(new Exam());
        examConfiguration.getExam().setName(examName);
        examConfiguration.getExam().setNumberOfTasks(numberOfTasks);
        examConfiguration.getExam().setTopics(new ArrayList<>());
        examConfiguration.getExam().setMaxGrade(maxGrade);

        examConfiguration.getExam().setTasks(taskList);

        Topic topic = new Topic();
        topic.setId(getTopicId(topicName));
        examConfiguration.getExam().getTopics().add(topic);

        Discipline discipline = new Discipline();
        discipline.setId(getDisciplineId(disciplineName));
        discipline.setName(disciplineName);

        examConfiguration.getExam().setDiscipline(discipline);

        when(getMetadataConnector().getDiscipline(anyLong())).thenReturn(discipline);
        when(getMetadataConnector().getTasksForTopic(anyLong())).thenReturn(taskList);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));

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
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "teacher"));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> responseEntity = doGet(url, requestEntity, PersonalExam.class);
        getTestExecutionContext().getDetails().setPersonalExamResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    private Long getTopicId(String name) {
        if ("Join".equals(name)) {
            return 1L;
        } else if ("SELECT".equals(name)) {
            return 2L;
        } else {
            return 3L;
        }
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

    @When("^this student start personal exam \"(.*)\"$")
    public void startPersonalExam(String personalExamName) throws JOSEException {
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentUid(personalExamName, getTestExecutionContext().getDetails().getStudentUid());
        String url = getBaseUrl() + "/api/personal-exam/" + input.getId() + "/start";
        log.info("Send request to " + url);
        HttpEntity<Task> requestEntity = startTest(getTestExecutionContext().getDetails().getStudentId().toString());
        ResponseEntity<Question> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Question.class);
        getTestExecutionContext().getDetails().setFirstTaskResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^this student start second time personal exam \"(.*)\"$")
    public void startSecondTimePersonalExam(String personalExamName) throws JOSEException {
        startSecondTimePersonalExam(getTestExecutionContext().getDetails().getStudentUid(), personalExamName);
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
        String url = "/api/personal-exam/student/" + userId;
        log.info("Send request to " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
        getTestExecutionContext().getDetails().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }
}
