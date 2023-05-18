package com.sytoss.producer.controllers;

import com.sytoss.producer.bom.ExamConfiguration;
import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.services.PersonalExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PersonalExamControllerTest extends AbstractControllerTest {

    @MockBean
    private PersonalExamService personalExamService;

    @InjectMocks
    private PersonalExamController personalExamController;

    @Test
    public void shouldSummaryExam() {
        when(personalExamService.summary(anyString())).thenReturn(new PersonalExam());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), headers);
        ResponseEntity<PersonalExam> result = doPost("/api/personalExam/123/summary", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
    }
}
