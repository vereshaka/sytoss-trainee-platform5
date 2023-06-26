package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
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

public class TopicControllerTest extends LessonsControllerTest {

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> result = doGet("/api/discipline/1/topics", httpEntity, new ParameterizedTypeReference<List<Topic>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldAssignTaskToTopic() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        when(taskService.assignTaskToTopic(anyLong(), anyLong())).thenReturn(new Task());
        ResponseEntity<Task> result = doPost("/api/task/1/topic/1", httpEntity, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnTopicById() {
        when(topicService.getById(anyLong())).thenReturn(new Topic());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Topic> result = doGet("/api/topic/123", httpEntity, Topic.class);
        assertEquals(200, result.getStatusCode().value());
    }
}
