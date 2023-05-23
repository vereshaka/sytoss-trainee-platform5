package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicConnector topicConnector;

    @Autowired
    private TopicConvertor topicConvertor;

    public List<Topic> findByDiscipline(String disciplineId) {
        List<TopicDTO> topicDTOList = topicConnector.findByDisciplineId(disciplineId);
        List<Topic> topicList = new ArrayList<>();
        for (TopicDTO topicDTO : topicDTOList) {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        }
        return topicList;
    }
}
