package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExamControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveExam() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> httpEntity = new HttpEntity<>(new Exam(), httpHeaders);
        ResponseEntity<List<Exam>> response = doPost("/api/exam/save", httpEntity, new ParameterizedTypeReference<List<Exam>>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }
}
