package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.config.AppConfig;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.connectors.UserConnector;
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
    public void shouldSaveTopic() throws JOSEException {
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("create_topic")));
        HttpEntity<Topic> requestEntity = new HttpEntity<>(new Topic(), headers);
        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, new ParameterizedTypeReference<Topic>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindGroupsByDiscipline() throws JOSEException {
        when(groupService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("find_groups_by_discipline")));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", requestEntity, new ParameterizedTypeReference<List<Group>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSaveDiscipline() throws JOSEException {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenReturn(new Discipline());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("create_discipline")));
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<Discipline> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<Discipline>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() throws JOSEException {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenThrow(new DisciplineExistException("SQL"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("create_discipline")));
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<String> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldGetDisciplineById() throws JOSEException {
        when(disciplineService.getById(any())).thenReturn(new Discipline());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("get_discipline")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Discipline> result = doGet("/api/discipline/123", httpEntity, Discipline.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetDisciplineByIdWhenItDoesNotExist() throws JOSEException {
        when(disciplineService.getById(any())).thenThrow(new DisciplineNotFoundException(123L));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("get_discipline")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/discipline/123", httpEntity, String.class);
        assertEquals(404, result.getStatusCode().value());
        assertEquals("Discipline with id \"123\" not found", result.getBody());
    }

    @Test
    public void shouldCreateGroup() throws JOSEException {
        when(groupService.create(anyLong(), any(Group.class))).thenReturn(new Group());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("create_group")));
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<Group> result = doPost("/api/discipline/123/group", requestEntity, Group.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateGroupWhenItExists() throws JOSEException {
        when(groupService.create(anyLong(), any(Group.class))).thenThrow(new GroupExistException("Test"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("create_group")));
        HttpEntity<Group> requestEntity = new HttpEntity<>(new Group(), headers);
        ResponseEntity<String> result = doPost("/api/discipline/123/group", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldFindTasksDomainByDiscipline() throws JOSEException {
        when(taskDomainService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("get_task_domains_by_discipline")));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<TaskDomain>> result = doGet("/api/discipline/1/taskDomains", requestEntity, new ParameterizedTypeReference<List<TaskDomain>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
