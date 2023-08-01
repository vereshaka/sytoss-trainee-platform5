package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.ExamService;
import com.sytoss.producer.services.PersonalExamService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ExamControllerTest extends StpApplicationTest {

    @InjectMocks
    private ExamController examController;

    @MockBean
    private ExamService examService;

    @Test
    public void shouldReturnPersonalExams() {
        when(examService.getPersonalExams(any())).thenReturn(List.of());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<PersonalExam>> result = doGet("/api/exam/1/personal-exams", requestEntity, new ParameterizedTypeReference<>(){});
        assertEquals(200, result.getStatusCode().value());
    }
}
