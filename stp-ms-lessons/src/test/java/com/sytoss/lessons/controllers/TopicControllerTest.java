package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.services.TaskService;
import com.sytoss.lessons.services.TopicService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TopicControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private TopicController topicController;

    @MockBean
    private TopicService topicService;

    @MockBean
    private TaskService taskService;

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<Topic>> result = doGet("/api/discipline/1/topics", null, new ParameterizedTypeReference<List<Topic>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldAssignTaskToTopic() {
        when(taskService.assignTaskToTopic(anyLong(), anyLong())).thenReturn(new Task());
        ResponseEntity<Task> result = doPost("/api/task/1/topic/1", null, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnTopicById() {
        when(topicService.getById(anyLong())).thenReturn(new Topic());
        ResponseEntity<Topic> result = doGet("/api/topic/123", null, new ParameterizedTypeReference<Topic>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
