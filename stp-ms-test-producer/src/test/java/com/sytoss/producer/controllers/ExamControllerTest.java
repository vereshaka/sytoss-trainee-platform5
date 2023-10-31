package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.services.ExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExamControllerTest extends TestProducerControllerTest {

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
        ResponseEntity<List<PersonalExam>> result = doGet("/api/exam/1/personal-exams", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
