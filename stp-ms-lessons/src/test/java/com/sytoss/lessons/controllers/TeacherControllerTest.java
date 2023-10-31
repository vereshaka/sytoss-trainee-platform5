package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends LessonsControllerTest {

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(disciplineService.findDisciplines(anyInt(),anyInt(), anyList())).thenReturn(Page.empty());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ResponseObject> result = doPost("/api/teacher/my/disciplines/0/5", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldGetListOfExams() {
        when(examService.findExams()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Exam>> response = doGet("/api/teacher/my/exams", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }
}
