package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.services.PersonalExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonalExamControllerTest extends AbstractControllerTest {

    @InjectMocks
    private PersonalExamController personalExamController;

    @MockBean
    private PersonalExamService personalExamService;

    @Test
    public void shouldCreateExam() {
        when(personalExamService.create(any())).thenReturn(new PersonalExam());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), headers);
        ResponseEntity<PersonalExam> result = doPost("/api/personalExam/create", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCodeValue());
    }
}
