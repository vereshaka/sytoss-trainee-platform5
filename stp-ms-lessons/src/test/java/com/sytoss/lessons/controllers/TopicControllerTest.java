package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TopicControllerTest extends LessonsControllerTest {

    @Test
    public void shouldAssignTaskToTopic() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        when(taskService.assignTaskToTopic(anyLong(), anyLong())).thenReturn(new Task());
        ResponseEntity<Task> result = doPost("/api/topic/1/task/1", httpEntity, Task.class);
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
