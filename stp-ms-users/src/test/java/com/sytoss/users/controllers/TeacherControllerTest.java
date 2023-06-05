package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TeacherControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private TeacherController teacherController;

    @MockBean
    private TeacherService teacherService;

    @Test
    public void shouldSaveTeacher() {
        when(teacherService.create(any())).thenReturn(new Teacher());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Teacher> requestEntity = new HttpEntity<>(new Teacher(), headers);
        ResponseEntity<Teacher> result = doPost("/api/teacher/", requestEntity, new ParameterizedTypeReference<Teacher>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
