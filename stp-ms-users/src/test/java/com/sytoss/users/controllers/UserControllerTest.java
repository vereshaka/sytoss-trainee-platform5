package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.stp.test.StpApplicationTest;
import com.sytoss.users.services.UserService;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest extends StpApplicationTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnProfileInfo() {
        when(userService.getOrCreateUser(anyString())).thenReturn(new Teacher());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Teacher> result = doGet("/api/user/me", requestEntity, Teacher.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldRetrieveMyDisciplines() {
        when(userService.findGroupsId()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Long>> result = doGet("/api/user/me/groupsId", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldUpdatePhoto() {
        byte[] photoBytes = {0x01, 0x02, 0x03};
        File photoFile;
        try {
            photoFile = File.createTempFile("photo", ".jpg");
            Files.write(photoFile.toPath(), photoBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", "name");
        body.add("middleName", "name");
        body.add("lastName", "name");
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Void> result = getRestTemplate().postForEntity(getEndpoint("/api/user/me"), requestEntity, Void.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void shouldReturnStudentGroups() {
        when(userService.findByStudent()).thenReturn(List.of(new Group()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Group>> responseEntity = doGet("/api/user/me/groups", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReturnStudentNotFoundException() {
        when(userService.findByStudent()).thenThrow(new UserNotFoundException("User not found"));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet("/api/user/me/groups", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(HttpStatus.valueOf(404), responseEntity.getStatusCode());
    }

    @Test
    public void shouldReturnUserPhoto() {
        byte[] iconBytes = {0x01, 0x02, 0x03};
        when(userService.getMyPhoto()).thenReturn(iconBytes);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/user/me/photo", httpEntity, byte[].class);
        assertEquals(200, result.getStatusCode().value());
    }
}
