package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskControllerTest extends AbstractApplicationTest {

    @InjectMocks
    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    @Test
    public void shouldGetDisciplineById() {
        when(taskService.getById(any())).thenReturn(new Task());
        ResponseEntity<Task> result = doGet("/api/task/5", null, Task.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetDisciplineByIdWhenItDoesNotExist() {
        when(taskService.getById(any())).thenThrow(new TaskNotFoundException(5L));
        ResponseEntity<String> result = doGet("/api/task/5", null, String.class);
        assertEquals(404, result.getStatusCode().value());
        assertEquals("Task with id \"5\" not found", result.getBody());
    }

}
