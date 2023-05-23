package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicConnector topicConnector;

    public List<Topic> findByDiscipline(String disciplineId) {
        return topicConnector.findByDisciplineId(disciplineId);
    }
}
