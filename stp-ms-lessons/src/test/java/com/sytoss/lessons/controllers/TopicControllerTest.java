package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TopicControllerTest extends LessonsControllerTest {


    @Test
    public void shouldAssignTaskToTopic() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List> requestEntity = new HttpEntity<>(List.of(1,2,3), httpHeaders);

        when(taskService.assignTasksToTopic(anyLong(), any(ArrayList.class))).thenReturn(List.of(new Task()));

        ResponseEntity<List<Task>> result = doPost("/api/topic/1/assign/tasks/", requestEntity, new ParameterizedTypeReference<>() {
        });
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

    @Test
    public void shouldReturnTopicIcon() {
        byte[] iconBytes = {0x01, 0x02, 0x03};
        when(topicService.getIcon(anyLong())).thenReturn(iconBytes);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/topic/4/icon", httpEntity, byte[].class);
        assertEquals(200, result.getStatusCode().value());
    }
}
