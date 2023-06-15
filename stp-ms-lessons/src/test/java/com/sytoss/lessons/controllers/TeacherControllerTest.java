package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.services.DisciplineService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends AbstractControllerTest {

    @InjectMocks
    @Autowired
    private TeacherController teacherController;

    @MockBean
    private DisciplineService disciplineService;

    @Test
    public void shouldFindGroupsByDiscipline() throws JOSEException {
        when(disciplineService.findDisciplines()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> result = doGet("/api/teacher/my/disciplines", httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}