package com.sytoss.users.controllers;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.services.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupControllerTest extends AbstractControllerTest {

    @InjectMocks
    private GroupController groupController;

    @MockBean
    private GroupService groupService;

    @Test
    public void shouldCreateGroup() {
        when(groupService.create(any(Group.class))).thenReturn(new Group());
        HttpHeaders headers = getDefaultHttpHeaders("teacher");
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<Group> responseEntity = doPost("/api/group", requestEntity, Group.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateGroupWhenItExists() {
        when(groupService.create(any(Group.class))).thenThrow(new GroupExistException("Test"));
        HttpHeaders headers = getDefaultHttpHeaders("teacher");
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<String> result = doPost("/api/group", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldAssignStudent() {
        HttpHeaders headers = getDefaultHttpHeaders("teacher");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = doPost("/api/group/5/student/9", requestEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }
}
