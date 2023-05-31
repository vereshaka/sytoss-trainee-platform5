package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.AbstractApplicationTest;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonalExamControllerTest extends AbstractApplicationTest {

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
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSummaryExam() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<PersonalExam> result = doGet("/api/personalExam/123/summary", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldStartTest() {
        when(personalExamService.start(anyString(), anyLong())).thenReturn(new Question());
        HttpHeaders headers = new HttpHeaders();
        headers.set("studentId", "1");
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Question> result = doGet("/api/test/123/start", requestEntity, Question.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testAnswer() {
        String examId = "123";
        Long studentID = 77L;
        String taskAnswer = "taskAnswer";
        Answer expectedAnswer = new Answer();

        when(answerService.answer(examId, studentID, taskAnswer)).thenReturn(expectedAnswer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("studentId", String.valueOf(studentID));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(taskAnswer, headers);

        ResponseEntity<Answer> result = doPost("/api/personalExam/12dsa/task/answer", requestEntity, Answer.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
