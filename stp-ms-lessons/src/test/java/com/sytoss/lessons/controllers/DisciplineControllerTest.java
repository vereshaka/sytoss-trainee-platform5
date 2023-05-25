package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.services.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

public class DisciplineControllerTest extends AbstractControllerTest {

    @InjectMocks
    private DisciplineController disciplineController;

    @MockBean
    private GroupService groupService;

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(groupService.findByDiscipline(any())).thenReturn(new  ArrayList<>());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", null, new ParameterizedTypeReference<List<Group>>(){});
        assertEquals(200, result.getStatusCode().value());
    }
}
