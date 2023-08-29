package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.TaskExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskControllerTest extends LessonsControllerTest {

    @Test
    public void shouldGetTakById() {
        when(taskService.getById(any())).thenReturn(new Task());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> result = doGet("/api/task/5", httpEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetTaskByIdWhenItDoesNotExist() {
        when(taskService.getById(any())).thenThrow(new TaskNotFoundException(5L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/task/5", httpEntity, String.class);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void shouldCreateTask() {
        when(taskService.create(any(Task.class))).thenReturn(new Task());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Task> requestEntity = new HttpEntity<>(new Task(), httpHeaders);
        ResponseEntity<Task> result = doPost("/api/task", requestEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotCreateTaskWhenItExists() {
        when(taskService.create(any(Task.class))).thenThrow(new TaskExistException("Test"));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Task> requestEntity = new HttpEntity<>(new Task(), httpHeaders);
        ResponseEntity<String> result = doPost("/api/task", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldRemoveConditionFromTask() {
        when(taskService.removeCondition(anyLong(), anyLong())).thenReturn(new Task());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> result = doPut("/api/task/12/condition/12", httpEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnListOfTasksByTopicId() {
        when(taskService.findByTopicId(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> result = doGet("/api/topic/1/tasks", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnQueryResult() {
        when(taskService.getQueryResult(any())).thenReturn(new QueryResult());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", 1);
        when(userConnector.getMyProfile()).thenReturn(teacherMap);
        HttpEntity<TaskDomainRequestParameters> requestEntity = new HttpEntity<>(new TaskDomainRequestParameters(), httpHeaders);
        ResponseEntity<QueryResult> result = doPost("/api/task/check-request-result", requestEntity, QueryResult.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnTopicsByTaskId() {
        when(taskService.getTopics(1L)).thenReturn(List.of(new Topic()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> result = doGet("/api/task/1/topics", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnExamsByTaskId() {
        when(taskService.getExams(1L)).thenReturn(List.of(new Exam()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Exam>> result = doGet("/api/task/1/exams", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());

    }
}
