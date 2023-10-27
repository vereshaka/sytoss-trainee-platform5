package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.TaskDomainScriptIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import com.sytoss.stp.test.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveTaskDomain() {
        when(taskDomainService.create(anyLong(), any())).thenReturn(new TaskDomain());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<TaskDomain> response = doPost("/api/discipline/123/task-domain", httpEntity, TaskDomain.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldUpdateTaskDomain() {
        when(taskDomainService.update(anyLong(), any())).thenReturn(new TaskDomain());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<TaskDomain> response = doPut("/api/task-domain/123", httpEntity, TaskDomain.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldNotUpdateTaskDomainWhenPersonalExamNotFinished() {
        when(taskDomainService.update(anyLong(), any())).thenThrow(new TaskDomainIsUsed("new"));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> requestEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<String> response = doPut("/api/task-domain/123", requestEntity, String.class);
        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() {
        when(taskDomainService.create(anyLong(), any(TaskDomain.class))).thenThrow(new TaskDomainAlreadyExist("SQL"));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> requestEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<String> result = doPost("/api/discipline/123/task-domain", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnTaskDomainById() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TaskDomain> responseEntity = doGet("/api/task-domain/1", httpEntity, TaskDomain.class);
        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldReturnTaskNotFoundException() {
        when(taskDomainService.getById(1L)).thenThrow(new TaskDomainNotFoundException(1L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet("/api/task-domain/1", httpEntity, String.class);
        Assertions.assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    public void ShouldReturnTaskDomainImage() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        String pumlScript = FileUtils.readFromFile("puml/script.puml");
        HttpEntity<String> httpEntity = new HttpEntity<>(pumlScript, httpHeaders);
        ResponseEntity<byte[]> responseEntity = doPut("/api/task-domain/puml/" + ConvertToPumlParameters.DB, httpEntity, new ParameterizedTypeReference<>() {
        });
        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldReturnCountOfTasks() {
        TaskDomainModel taskDomainModel = new TaskDomainModel();
        taskDomainModel.setCountOfTasks(1);
        when(taskDomainService.getCountOfTasks((any()))).thenReturn(taskDomainModel);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TaskDomainModel> responseEntity = doGet("/api/task-domain/1/overview", httpEntity, TaskDomainModel.class);
        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
        Assertions.assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getCountOfTasks());
    }


    @Test
    public void shouldReturnTasks() {
        when(taskDomainService.getTasks(any())).thenReturn(List.of(new Task()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", 1);
        when(userConnector.getMyProfile()).thenReturn(teacherMap);
        HttpEntity<TaskDomainRequestParameters> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> result = doGet("/api/task-domain/1/tasks", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotSaveTaskDomainWhenScriptIsNotValid() {
        when(taskDomainService.create(anyLong(), any())).thenThrow(TaskDomainScriptIsNotValidException.class);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<TaskDomain> response = doPost("/api/discipline/123/task-domain", httpEntity, TaskDomain.class);
        assertEquals(400, response.getStatusCode().value());
    }
}
