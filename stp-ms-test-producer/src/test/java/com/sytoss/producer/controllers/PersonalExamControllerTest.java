package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.UserConnector;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PersonalExamControllerTest extends StpApplicationTest {

    @InjectMocks
    private PersonalExamController personalExamController;

    @MockBean
    private PersonalExamService personalExamService;

    @MockBean
    private AnswerService answerService;

    @MockBean
    private UserConnector userConnector;

    @Test
    public void shouldCreateExam() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        PersonalExam personalExam = new PersonalExam();
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.ANSWERED);
        personalExam.setAnswers(List.of(answer));
        when(personalExamService.create(any())).thenReturn(personalExam);
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), httpHeaders);
        ResponseEntity<PersonalExam> result = doPost("/api/personal-exam/create", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
        personalExam = result.getBody();
        assertEquals(0, personalExam.getAnswers().size());
    }

    @Test
    public void shouldReturnBooleanConditionIfTaskDomainIsUsed() {
        when(personalExamService.taskDomainIsUsed(anyLong())).thenReturn(true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Boolean> result = doGet("/api/personal-exam/is-used-now/task-domain/123", requestEntity, boolean.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSummaryExam() {
        PersonalExam personalExam = new PersonalExam();
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.ANSWERED);
        personalExam.setAnswers(List.of(answer));
        when(personalExamService.summary(any())).thenReturn(personalExam);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> result = doGet("/api/personal-exam/123/summary", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
        personalExam = result.getBody();
        assertEquals(1, personalExam.getAnswers().size());
    }

    @Test
    public void shouldReturnQuestionImage() {
        when(personalExamService.getQuestionImage("123-abc-def")).thenReturn(new byte[]{});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/personal-exam/123-abc-def/task/question", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldStartTest() throws JOSEException {
        when(personalExamService.start(anyString())).thenReturn(new Question());
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<Question> result = doGet("/api/personal-exam/123/start", requestEntity, Question.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testAnswer() throws JOSEException {
        String examId = "123";
        String taskAnswer = "taskAnswer";
        Date uiDate = new Date();
        Long timeSpent = 10L;
        Question expectedAnswer = new Question();
        AnswerModule answerModule = new AnswerModule();
        answerModule.setAnswer(taskAnswer);
        answerModule.setAnswerUIDate(uiDate);
        answerModule.setTimeSpent(timeSpent);

        when(answerService.answer(examId, taskAnswer, uiDate, timeSpent)).thenReturn(expectedAnswer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnswerModule> requestEntity = new HttpEntity<>(answerModule, httpHeaders);

        ResponseEntity<Question> result = doPost("/api/personal-exam/12dsa/task/answer", requestEntity, Question.class);
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
    }

    @Test
    public void shouldGetPersonalExamByUserId() throws ParseException, JOSEException {
        List<PersonalExam> exams = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        exams.add(createPersonalExam(1L, "Math", 5, format.parse("14.12.2018"), format.parse("14.12.2018")));
        exams.add(createPersonalExam(2L, "SQL", 10, format.parse("14.12.2018"), format.parse("14.12.2018")));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        when(personalExamService.getByStudentId(1L)).thenReturn(exams);

        ResponseEntity<List<Task>> result = doGet("/api/personal-exam/teacher/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void shouldDeletePersonalExamsByExamId() throws JOSEException {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<PersonalExam>> result = doDelete("/api/personal-exam/exam/assignee/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    private PersonalExam createPersonalExam(Long examAssigneeId, String name, int amountOfTasks, Date assignedDate, Date startedDate) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setExamAssigneeId(examAssigneeId);
        personalExam.setName(name);
        personalExam.finish();
        personalExam.setAmountOfTasks(amountOfTasks);
        personalExam.setAssignedDate(assignedDate);
        personalExam.setStartedDate(startedDate);

        return personalExam;
    }

    @Test
    public void shouldReviewByAnswers() throws JOSEException {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);

        Answer answer1 = new Answer();
        answer1.setValue("select * from products");
        answer1.setGrade(new Grade(1, "answer correct"));
        answer1.setStatus(AnswerStatus.GRADED);
        Answer answer2 = new Answer();
        answer2.setValue("select * from products");
        answer2.setGrade(new Grade(1, "answer correct"));
        answer2.setStatus(AnswerStatus.GRADED);
        answer1.setId(1L);
        answer2.setId(2L);

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("1");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(answer1));

        PersonalExam personalExam2 = new PersonalExam();
        personalExam2.setId("2");
        personalExam2.setName("DDL requests");
        personalExam2.setAnswers(List.of(answer2));

        when(personalExamService.getById("1")).thenReturn(personalExam);
        when(personalExamService.getById("2")).thenReturn(personalExam2);

        answer1.setTeacherGrade(new Grade(20, "ok"));
        answer2.setTeacherGrade(new Grade(10, "ok"));

        ExamAssigneeAnswersModel examAssigneeAnswersModel = new ExamAssigneeAnswersModel();
        ReviewGradeModel gradeModel1 = new ReviewGradeModel();
        gradeModel1.setPersonalExamId(personalExam.getId());
        gradeModel1.setAnswer(answer1);
        ReviewGradeModel gradeModel2 = new ReviewGradeModel();
        gradeModel2.setPersonalExamId(personalExam2.getId());
        gradeModel2.setAnswer(answer2);
        examAssigneeAnswersModel.setGrades(List.of(gradeModel1, gradeModel2));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<ExamAssigneeAnswersModel> httpEntity = new HttpEntity<>(examAssigneeAnswersModel, httpHeaders);

        ResponseEntity<ExamAssigneeAnswersModel> result = doPost("/api/personal-exam/review/answers", httpEntity, ExamAssigneeAnswersModel.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
