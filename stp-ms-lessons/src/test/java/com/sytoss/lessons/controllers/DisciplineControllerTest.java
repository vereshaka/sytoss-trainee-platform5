package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import org.junit.jupiter.api.Test;
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

public class DisciplineControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveTopic() {
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Topic> requestEntity = new HttpEntity<>(new Topic(), headers);
        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, Topic.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(disciplineService.getGroups(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindDisciplinesByStudent() {
        when(disciplineService.findAllMyDiscipline()).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> result = doGet("/api/my/disciplines", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSaveDiscipline() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenReturn(new Discipline());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<Discipline> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() {
        when(disciplineService.create(anyLong(), any(Discipline.class))).thenThrow(new DisciplineExistException("SQL"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<String> result = doPost("/api/teacher/7/discipline", requestEntity, new ParameterizedTypeReference<>() {
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
    public void shouldFindTasksDomainByDiscipline() {
        when(taskDomainService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<TaskDomain>> result = doGet("/api/discipline/1/task-domains", requestEntity, new ParameterizedTypeReference<List<TaskDomain>>() {
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

    @Test
    public void shouldLinkGroupToDiscipline() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> result = doPost("/api/discipline/2/group/5", httpEntity, Void.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnDisciplineIcon() {
        byte[] iconBytes = {0x01, 0x02, 0x03};
        when(disciplineService.getIcon(anyLong())).thenReturn(iconBytes);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/discipline/4/icon", httpEntity, byte[].class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> result = doGet("/api/discipline/1/topics", httpEntity, new ParameterizedTypeReference<List<Topic>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
