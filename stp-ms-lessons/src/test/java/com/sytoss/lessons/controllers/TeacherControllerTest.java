package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.controllers.api.ResponseObject;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends LessonsControllerTest {

    @Test
    public void shouldFindGroupsByDiscipline() throws JOSEException {
        when(disciplineService.findDisciplines(anyInt(), anyInt(), anyList())).thenReturn(Page.empty());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ResponseObject> result = doPost("/api/teacher/my/disciplines/0/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldGetListOfExams() {
        when(examService.findExams()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Exam>> response = doGet("/api/teacher/my/exams", httpEntity, new ParameterizedTypeReference<List<Exam>>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }
}
