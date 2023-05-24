package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonalExamControllerTest extends AbstractControllerTest {

    @InjectMocks
    private PersonalExamController personalExamController;

    @MockBean
    private PersonalExamService personalExamService;

    @MockBean
    private AnswerService answerService;

    @Test
    public void shouldCreateExam() {
        when(personalExamService.create(any())).thenReturn(new PersonalExam());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), headers);
        ResponseEntity<PersonalExam> result = doPost("/api/personalExam/create", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldSummaryExam() {
        ResponseEntity<PersonalExam> result = doGet("/api/personalExam/123/summary", Void.class, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldStartTest() {
        when(personalExamService.start(any())).thenReturn(new Task());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Task> result = doGet("/api/test/123/start", requestEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testAnswer() {
        String examId = "examId";
        String taskAnswerValue = "taskAnswer";
        HttpEntity<String> taskAnswer = new HttpEntity<>(taskAnswerValue);

        Answer expectedAnswer = new Answer();
        ResponseEntity<Answer> expectedResponse = new ResponseEntity<>(expectedAnswer, HttpStatus.OK);

        when(answerService.answer(examId, taskAnswer.getBody())).thenReturn(expectedAnswer);

        ResponseEntity<Answer> actualResponse = personalExamController.answer(examId, taskAnswer);

        assertEquals(expectedResponse, actualResponse);
        verify(answerService).answer(examId, taskAnswer.getBody());
    }
}