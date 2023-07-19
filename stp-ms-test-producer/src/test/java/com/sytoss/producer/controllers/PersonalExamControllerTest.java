package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

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
        Answer expectedAnswer = new Answer();

        when(answerService.answer(examId, taskAnswer)).thenReturn(expectedAnswer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(taskAnswer, httpHeaders);

        ResponseEntity<Answer> result = doPost("/api/personal-exam/12dsa/task/answer", requestEntity, Answer.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
