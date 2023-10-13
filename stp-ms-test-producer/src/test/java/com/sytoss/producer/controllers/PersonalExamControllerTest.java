package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.AnswerModule;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.connectors.UserConnector;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
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
        when(personalExamService.create(any())).thenReturn(new PersonalExam());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), httpHeaders);
        ResponseEntity<PersonalExam> result = doPost("/api/personal-exam/create", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> result = doGet("/api/personal-exam/123/summary", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
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

    @Test
    public void shouldReturnExcelReport() throws JOSEException, IOException {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        when(personalExamService.getExcelReport(1L)).thenReturn(new byte[]{});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/personal-exam/excel/assignee/1", httpEntity, String.class);
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
}
