package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.GroupService;
import com.sytoss.lessons.services.TaskDomainService;
import com.sytoss.lessons.services.TopicService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
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

public class DisciplineControllerTest extends AbstractControllerTest {

    @InjectMocks
    @Autowired
    private DisciplineController disciplineController;

    @MockBean
    private TopicService topicService;

    @MockBean
    private TopicConnector topicConnector;

    @MockBean
    private GroupService groupService;

    @MockBean
    private DisciplineService disciplineService;

    @MockBean
    private TaskDomainService taskDomainService;

    @Test
    public void shouldSaveTopic() {
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Topic> requestEntity = new HttpEntity<>(new Topic(), headers);
        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, new ParameterizedTypeReference<Topic>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(groupService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", requestEntity, new ParameterizedTypeReference<List<Group>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSaveDiscipline() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenReturn(new Discipline());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<Discipline> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<Discipline>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenThrow(new DisciplineExistException("SQL"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<String> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldGetDisciplineById() {
        when(disciplineService.getById(any())).thenReturn(new Discipline());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Discipline> result = doGet("/api/discipline/123", httpEntity, Discipline.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetDisciplineByIdWhenItDoesNotExist() {
        when(disciplineService.getById(any())).thenThrow(new DisciplineNotFoundException(123L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/discipline/123", httpEntity, String.class);
        assertEquals(404, result.getStatusCode().value());
        assertEquals("Discipline with id \"123\" not found", result.getBody());
    }

    @Test
    public void shouldCreateGroup() {
        when(groupService.create(anyLong(), any(Group.class))).thenReturn(new Group());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<Group> result = doPost("/api/discipline/123/group", requestEntity, Group.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateGroupWhenItExists() {
        when(groupService.create(anyLong(), any(Group.class))).thenThrow(new GroupExistException("Test"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<String> result = doPost("/api/discipline/123/group", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldFindTasksDomainByDiscipline() {
        when(taskDomainService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<TaskDomain>> result = doGet("/api/discipline/1/taskDomains", requestEntity, new ParameterizedTypeReference<List<TaskDomain>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindDisciplines() {
        when(disciplineService.findAllDisciplines()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> result = doGet("/api/disciplines", httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
