package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.exceptions.business.TaskExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
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

public class TaskControllerTest extends LessonsControllerTest {

    @Test
    public void shouldGetTakById() throws JOSEException {
        when(taskService.getById(any())).thenReturn(new Task());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> result = doGet("/api/task/5", httpEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetTaskByIdWhenItDoesNotExist() throws JOSEException {
        when(taskService.getById(any())).thenThrow(new TaskNotFoundException(5L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/task/5", httpEntity, String.class);
        assertEquals(404, result.getStatusCode().value());
        assertEquals("Task with id \"5\" not found", result.getBody());
    }

    @Test
    public void shouldCreateTask() throws JOSEException {
        when(taskService.create(any(Task.class))).thenReturn(new Task());
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Task> requestEntity = new HttpEntity<>(new Task(), headers);
        ResponseEntity<Task> result = doPost("/api/task/", requestEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateTaskWhenItExists() throws JOSEException {
        when(taskService.create(any(Task.class))).thenThrow(new TaskExistException("Test"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Task> requestEntity = new HttpEntity<>(new Task(), headers);
        ResponseEntity<String> result = doPost("/api/task/", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldRemoveConditionFromTask() throws JOSEException {
        when(taskService.removeCondition(anyLong(), anyLong())).thenReturn(new Task());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> result = doPut("/api/task/12/condition/12", httpEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnListOfTasksByTopicId() throws JOSEException {
        when(taskService.findByTopicId(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> result = doGet("/api/topic/1/tasks", httpEntity, new ParameterizedTypeReference<List<Task>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
