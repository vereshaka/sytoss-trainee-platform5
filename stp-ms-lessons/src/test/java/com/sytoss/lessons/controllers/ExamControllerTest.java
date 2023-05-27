package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.services.ExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExamControllerTest extends AbstractControllerTest {

    @InjectMocks
    private ExamController examController;

    @MockBean
    private ExamService examService;

    @Test
    public void shouldSaveExam() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(new Exam(), headers);

        ResponseEntity<Void> response = doPost("/api/exam/save", requestEntity, Void.class);
        assertEquals(200, response.getStatusCode().value());
    }
}
