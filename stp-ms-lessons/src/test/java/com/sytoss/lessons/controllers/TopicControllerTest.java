package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Topic;
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
import static org.mockito.Mockito.when;

public class TopicControllerTest extends AbstractControllerTest {

    @InjectMocks
    private TopicController topicController;

    @MockBean
    private TopicService topicService;

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<Topic>> result = doGet("/api/discipline/1/topics", null, new ParameterizedTypeReference<List<Topic>>(){});
        assertEquals(200, result.getStatusCode().value());
    }
}
