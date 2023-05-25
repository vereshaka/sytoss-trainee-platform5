package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.services.TopicService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DisciplineControllerTest extends AbstractControllerTest {

    @InjectMocks
    private DisciplineController disciplineController;

    @MockBean
    private TopicService topicService;

    @MockBean
    private TopicConnector topicConnector;

    @Test
    public void shouldSaveTopic() {
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Topic> requestEntity = new HttpEntity<>(new Topic(), headers);
        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, Topic.class);
        assertEquals(200, result.getStatusCode().value());
    }

}
