package com.sytoss.producer.controllers;

import com.sytoss.producer.bom.Task;
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

public class PersonalExamControllerTest extends AbstractControllerTest{

    @InjectMocks
    private PersonalExamController personalExamController;

    @MockBean
    private PersonalExamService personalExamService;

    @Test
    public void shouldStartTest() {
        when(personalExamService.start(any())).thenReturn(new Task());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Task> result = doGet("/api/test/123/start", requestEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }
}