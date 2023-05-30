package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.businessException.DisciplineExistException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.GroupService;
import com.sytoss.lessons.services.TopicService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DisciplineControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private DisciplineController disciplineController;

    @MockBean
    private TopicService topicService;

    @MockBean
    private TopicConnector topicConnector;

    @MockBean
    private GroupService groupService;

    @MockBean
    private DisciplineService disciplineService;

    @Test
    public void shouldSaveTopic() {
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Topic> requestEntity = new HttpEntity<>(new Topic(), headers);
        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, new ParameterizedTypeReference<Topic>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(groupService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", null, new ParameterizedTypeReference<List<Group>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSaveDiscipline() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenReturn(new Discipline());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<Discipline> result = doPost("/api/teacher/7/discipline/create", requestEntity, new ParameterizedTypeReference<Discipline>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void shouldReturn409Conflict() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenThrow(new DisciplineExistException("SQL"));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<String> result = doPost("/api/teacher/7/discipline/create", requestEntity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(409, result.getStatusCode().value());
    }
}
