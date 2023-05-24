package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractLessonsApplicationTest;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TopicServiceTest extends AbstractLessonsApplicationTest {

    @MockBean
    private TopicConnector topicConnector;

    @InjectMocks
    private TopicService topicService;

    @MockBean
    private TopicConvertor topicConvertor;

    @Test
    public void createExam() {
        List<TopicDTO> topics = new ArrayList<>();
        topics.add(createTopic("topic first"));
        topics.add(createTopic("topic second"));
        topics.add(createTopic("topic third"));
        when(topicConnector.findByDisciplineId(any())).thenReturn(topics);
        List<Topic> topicAnswer = topicService.findByDiscipline(1L);
        assertEquals(3, topicAnswer.size());
    }

    private TopicDTO createTopic(String name) {
        TopicDTO topic = new TopicDTO();
        topic.setName(name);
        return topic;
    }
}
