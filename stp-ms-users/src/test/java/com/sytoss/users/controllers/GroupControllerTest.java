package com.sytoss.users.controllers;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.stp.test.StpApplicationTest;
import com.sytoss.users.services.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupControllerTest extends StpApplicationTest {

    @InjectMocks
    private GroupController groupController;

    @MockBean
    private GroupService groupService;

    @Test
    public void shouldCreateGroup() {
        when(groupService.create(any(Group.class))).thenReturn(new Group());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), httpHeaders);
        ResponseEntity<Group> responseEntity = doPost("/api/group", requestEntity, Group.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateGroupWhenItExists() {
        when(groupService.create(any(Group.class))).thenThrow(new GroupExistException("Test"));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), httpHeaders);
        ResponseEntity<String> result = doPost("/api/group", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldAssignStudent() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doPost("/api/group/5/student/9", requestEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldGetGroup() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", ""));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doGet("/api/group/5", requestEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }
}
