package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends LessonsControllerTest {

    @Test
    public void shouldFindGroupsByDiscipline() throws JOSEException {
        when(disciplineService.findDisciplines()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> result = doGet("/api/teacher/my/disciplines", httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
