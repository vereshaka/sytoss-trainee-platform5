package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.services.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupControllerTest extends AbstractControllerTest {

    @InjectMocks
    private GroupController groupController;

    @MockBean
    private GroupService groupService;

    @Test
    public void shouldAssignStudent() {
        HttpHeaders headers = getDefaultHttpHeaders("teacher");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = doPost("/api/group/5/student/9", requestEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }
}
