package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.TopicExistException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicConnector topicConnector;

    @Autowired
    private TopicConvertor topicConvertor;

    @Autowired
    @Lazy
    private DisciplineService disciplineService;

    public List<Topic> findByDiscipline(Long disciplineId) {
        List<TopicDTO> topicDTOList = topicConnector.findByDisciplineId(disciplineId);
        List<Topic> topicList = new ArrayList<>();
        for (TopicDTO topicDTO : topicDTOList) {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        }
        return topicList;
    }

    public Topic create(Long disciplineId, Topic topic) {
        TopicDTO oldTopicDTO = topicConnector.getByNameAndDisciplineId(topic.getName(), disciplineId);
        Discipline discipline = disciplineService.getById(disciplineId);
        if (oldTopicDTO == null) {
            topic.setDiscipline(discipline);
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            topicDTO = topicConnector.saveAndFlush(topicDTO);
            topicConvertor.fromDTO(topicDTO, topic);
            return topic;
        } else {
            throw new TopicExistException(topic.getName());
        }
    }
}
