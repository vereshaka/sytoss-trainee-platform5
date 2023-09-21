package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExamControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveExam() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> httpEntity = new HttpEntity<>(new Exam(), httpHeaders);
        ResponseEntity<List<Exam>> response = doPost("/api/exam/save", httpEntity, new ParameterizedTypeReference<List<Exam>>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldDeleteExam() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Exam> response = doDelete("/api/exam/1/delete", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }
}
