package com.sytoss.users.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserControllerTest extends AbstractControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnProfileInfo() {
        when(userService.getOrCreateUser(anyString())).thenReturn(new Teacher());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Teacher> result = doGet("/api/user/me", requestEntity, Teacher.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldUpdatePhoto() {
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        File photoFile;
        try {
            photoFile = File.createTempFile("photo", ".jpg");
            Files.write(photoFile.toPath(), photoBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123"), null, null, null, "teacher"));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", "name");
        body.add("lastName", "name");
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> result = getRestTemplate().postForEntity(getEndpoint("/api/user/me"), requestEntity, Void.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
