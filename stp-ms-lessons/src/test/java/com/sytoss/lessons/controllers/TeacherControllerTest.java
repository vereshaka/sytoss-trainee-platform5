package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractApplicationTest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends AbstractApplicationTest {

    @InjectMocks
    @Autowired
    private TeacherController teacherController;

    @MockBean
    private DisciplineService disciplineService;

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(disciplineService.findDisciplines()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Discipline>> result = doGet("/api/teacher/my/disciplines", null, new ParameterizedTypeReference<List<Discipline>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}