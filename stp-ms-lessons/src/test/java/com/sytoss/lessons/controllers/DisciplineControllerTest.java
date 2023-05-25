package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.businessException.notFound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.TopicService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DisciplineControllerTest extends AbstractControllerTest {

    @InjectMocks
    private DisciplineController disciplineController;

    @MockBean
    private DisciplineService disciplineService;

    @Test
    public void shouldGetDisciplineById(){
        when(disciplineService.getById(any())).thenReturn(new Discipline());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<Discipline> result = doGet("/api/discipline/123", requestEntity, Discipline.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldNotGetDisciplineByIdWhenItDoesNotExist(){
        when(disciplineService.getById(any())).thenThrow(new DisciplineNotFoundException(123L));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(new Discipline(), headers);
        ResponseEntity<String> result = doGet("/api/discipline/123", requestEntity, String.class);
        assertEquals(404, result.getStatusCodeValue());
        assertEquals("Discipline with id 123 not found!", result.getBody());
    }
}
