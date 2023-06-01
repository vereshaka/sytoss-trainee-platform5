package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TopicExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TeacherNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TopicNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.TopicDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TopicService {

    private final TopicConnector topicConnector;

    private final TopicConvertor topicConvertor;

    private final DisciplineService disciplineService;

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

    public Topic getById(Long id) {
        try {
            TopicDTO topicDTO = topicConnector.getReferenceById(id);
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            return topic;
        } catch (EntityNotFoundException e) {
            throw new TopicNotFoundException(id);
        }
    }
}
