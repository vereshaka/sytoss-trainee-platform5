package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
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

public class TopicControllerTest extends AbstractControllerTest {

    @InjectMocks
    private TopicController topicController;

    @MockBean
    private TopicService topicService;

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List> result = doGet("/api/12dsa/topics", null, List.class);
        assertEquals(200, result.getStatusCode().value());
    }
}
