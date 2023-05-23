package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractLessonsApplicationTest;
import com.sytoss.lessons.connectors.TopicConnector;
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

    @Test
    public void createExam() {
        List<Topic> topics = new ArrayList<>();
        topics.add(createTopic("topic first"));
        topics.add(createTopic("topic second"));
        topics.add(createTopic("topic third"));
        when(topicConnector.findByDisciplineId(any())).thenReturn(topics);
        List<Topic> topicAnswer = topicService.findByDiscipline("das13ads");
        assertEquals(3, topicAnswer.size());
    }

    private Topic createTopic(String name) {
        Topic topic = new Topic();
        topic.setName(name);
        return topic;
    }
}
