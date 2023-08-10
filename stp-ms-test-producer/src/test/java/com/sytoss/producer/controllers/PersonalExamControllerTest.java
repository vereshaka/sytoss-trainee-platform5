package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
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
import java.sql.Date;
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

    @Test
    public void shouldGetPersonalExamByUserId() throws ParseException, JOSEException {
        List<PersonalExam> exams = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        exams.add(createPersonalExam(1L,"Math", 5,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));
        exams.add(createPersonalExam(2L,"SQL", 10,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "1"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        when(personalExamService.getByStudentId(1L)).thenReturn(exams);

        ResponseEntity<List<Task>> result = doGet("/api/personal-exam/teacher/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private PersonalExam createPersonalExam(Long examId, String name, int amountOfTasks, Date assignedDate, Date startedDate) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setExamId(examId);
        personalExam.setName(name);
        personalExam.setStatus(PersonalExamStatus.FINISHED);
        personalExam.setAmountOfTasks(amountOfTasks);
        personalExam.setAssignedDate(assignedDate);
        personalExam.setStartedDate(startedDate);

        return personalExam;
    }
}
