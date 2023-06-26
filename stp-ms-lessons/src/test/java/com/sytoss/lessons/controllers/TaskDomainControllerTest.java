package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.TaskDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveTaskDomain() throws JOSEException {
        when(taskDomainService.create(anyLong(), any())).thenReturn(new TaskDomain());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<TaskDomain> response = doPost("/api/discipline/123", httpEntity, TaskDomain.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldUpdateTaskDomain() throws JOSEException {
        when(taskDomainService.update(anyLong(), any())).thenReturn(new TaskDomain());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(new TaskDomain(), httpHeaders);
        ResponseEntity<TaskDomain> response = doPut("/api/taskDomain/123", httpEntity, TaskDomain.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldNotUpdateTaskDomainWhenPersonalExamNotFinished() {
        when(taskDomainService.update(anyLong(), any())).thenThrow(new TaskDomainIsUsed("new"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> requestEntity = new HttpEntity<>(new TaskDomain(), headers);
        ResponseEntity<String> response = doPut("/api/taskDomain/123", requestEntity, String.class);
        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() throws JOSEException {
        when(taskDomainService.create(anyLong(), any(TaskDomain.class))).thenThrow(new TaskDomainAlreadyExist("SQL"));
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> requestEntity = new HttpEntity<>(new TaskDomain(), headers);
        ResponseEntity<String> result = doPost("/api/discipline/123", requestEntity, String.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnTaskDomainById() throws JOSEException {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TaskDomain> responseEntity = doGet("/api/taskDomain/1", httpEntity, TaskDomain.class);
        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldReturnTaskNotFoundException() throws JOSEException {
        when(taskDomainService.getById(1L)).thenThrow(new TaskDomainNotFoundException(1L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet("/api/taskDomain/1", httpEntity, String.class);
        Assertions.assertEquals(404, responseEntity.getStatusCode().value());
    }
}
