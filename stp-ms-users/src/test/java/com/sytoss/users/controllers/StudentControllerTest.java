package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.services.StudentService;
import com.sytoss.users.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    public void shouldSaveTeacher() {

        MultipartFile photo = mock(MultipartFile.class);

        try {
            when(studentService.updatePhoto(any(), any())).thenReturn(photo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultipartFile> requestEntity = new HttpEntity<>(photo, headers);
        ResponseEntity<MultipartFile> result = doPost("/api/student/updatePhoto", requestEntity, new ParameterizedTypeReference<MultipartFile>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
