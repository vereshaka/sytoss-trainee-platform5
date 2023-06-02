package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.services.TaskDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskDomainControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private TaskDomainController taskDomainController;

    @MockBean
    private TaskDomainService taskDomainService;

    @Test
    public void shouldSaveTaskDomain() {
        ResponseEntity<TaskDomain> response = doPost("/api/taskDomain/", new TaskDomain(), new ParameterizedTypeReference<TaskDomain>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() {
        when(taskDomainService.create(any(TaskDomain.class))).thenThrow(new TaskDomainAlreadyExist("SQL"));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TaskDomain> requestEntity = new HttpEntity<>(new TaskDomain(), headers);
        ResponseEntity<String> result = doPost("/api/taskDomain/", requestEntity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(409, result.getStatusCode().value());
    }
}
