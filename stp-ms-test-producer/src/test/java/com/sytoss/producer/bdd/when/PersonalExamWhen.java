package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        Topic topic = new Topic();
        topic.setId(1L);
        long id = 0;
        for (String task : tasks) {
            Task newTask = new Task();
            newTask.setId(id);
            newTask.setCoef(1d);
            newTask.setQuestion(task);
            newTask.setTopics(List.of(topic));
            taskList.add(newTask);
            ++id;
        }

        ExamConfiguration examConfiguration = new ExamConfiguration();
        Student student = new Student();
        student.setUid(studentId);
        examConfiguration.setStudent(student);

        ExamAssignee examAssignee = new ExamAssignee();
        examAssignee.setRelevantTo(new Date());
        examAssignee.setRelevantFrom(new Date());
        examAssignee.setExam(examConfiguration.getExam());
        examConfiguration.setExamAssignee(new ExamAssignee());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        examConfiguration.getExamAssignee().setRelevantFrom(dateFormat.parse(relevantFrom));
        examConfiguration.getExamAssignee().setRelevantTo(dateFormat.parse(relevantTo));

        Teacher teacher = new Teacher();
        teacher.setFirstName("Teacher");

        examConfiguration.setExam(new Exam());
        examConfiguration.getExam().setName(examName);
        examConfiguration.getExam().setNumberOfTasks(numberOfTasks);
        examConfiguration.getExam().setTopics(new ArrayList<>());
        examConfiguration.getExam().setMaxGrade(maxGrade);
        examConfiguration.getExam().setTeacher(teacher);

        examConfiguration.getExam().setTasks(taskList);

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
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Teacher"));

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
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Teacher"));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> responseEntity = doGet(url, requestEntity, PersonalExam.class);
        getTestExecutionContext().getDetails().setPersonalExamResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^student path to summary to exam with id (.*)$")
    public void studentPathToSummary(String personalExamId) {
        String url = URI + "personal-exam/" + personalExamId + "/summary";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Student"));
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

    @When("operation for retrieving personal exams for this student")
    public void retrievePersonalExamsForStudent() {
        String url = "/api/personal-exam/me";
        log.info("Send request to " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(getTestExecutionContext().getToken());
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

    @When("^the exam with id (.*) is reviewed$")
    public void theExamWithIdIsReviewed(String id) {
        String url = "/api/personal-exam/review";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Teacher"));
        HttpEntity<PersonalExam> requestEntity = new HttpEntity<>(getTestExecutionContext().getDetails().getPersonalExam(), httpHeaders);
        ResponseEntity<PersonalExam> responseEntity = doPost(url, requestEntity, PersonalExam.class);
        getTestExecutionContext().getDetails().setPersonalExamResponse(responseEntity);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }

    @When("^the teacher gets personal exam by discipline (.*), exam assignees (.*) and students (.*)$")
    public void theTeacherGetsPersonalExamByDisciplineExamAssigneesAndStudents(String disciplineId, String examAssignees, String students) throws JOSEException {
        String url = "/api/personal-exam/students";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        PersonalExamByStudentsModel personalExamByStudentsModel = new PersonalExamByStudentsModel();
        personalExamByStudentsModel.setDisciplineId(Long.parseLong(getTestExecutionContext().replaceId(disciplineId).toString()));
        personalExamByStudentsModel.setExamAssignees(Arrays.stream(examAssignees.split(","))
                .map(el -> Long.parseLong(getTestExecutionContext().replaceId(el).toString())).toList());
        personalExamByStudentsModel.setStudents(Arrays.stream(students.split(",")).map(el -> {
            Student student = new Student();
            student.setId(Long.parseLong(el));
            return student;
        }).toList());
        HttpEntity<PersonalExamByStudentsModel> requestEntity = new HttpEntity<>(personalExamByStudentsModel, httpHeaders);
        ResponseEntity<List<PersonalExam>> responseEntity = doPost(url, requestEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
        getTestExecutionContext().getDetails().setPersonalExams(responseEntity.getBody());
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
    }
}
