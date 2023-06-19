package com.sytoss.users.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest extends AbstractControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void shouldSaveTeacher() throws JOSEException {
        when(userService.getOrCreateUser(anyString())).thenReturn(new Teacher());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123"), null, null, null, "teacher"));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Teacher> result = doGet("/api/user/me", requestEntity, Teacher.class);
        assertEquals(200, result.getStatusCode().value());
    }
}
