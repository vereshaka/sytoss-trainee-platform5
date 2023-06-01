package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.services.ExamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExamControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private ExamController examController;

    @MockBean
    private ExamService examService;

    @Test
    public void shouldSaveExam() {
        ResponseEntity<Exam> response = doPost("/api/exam/save", new Exam(), new ParameterizedTypeReference<Exam>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }
}
