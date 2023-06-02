package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    @Test
    public void shouldReturnListOfTasksByTopicId() {
        when(taskService.findByTopicId(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<Task>> result = doGet("/api/topic/1/tasks", null, new ParameterizedTypeReference<List<Task>>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
