package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TopicExistException;
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


@Slf4j
@Service
@RequiredArgsConstructor
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
        Discipline discipline = disciplineService.getById(disciplineId);
        TopicDTO oldTopicDTO = topicConnector.getByNameAndDisciplineId(topic.getName(), disciplineId);
        if (oldTopicDTO == null) {
            topic.setDiscipline(discipline);
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            topicDTO = topicConnector.saveAndFlush(topicDTO);
            topicConvertor.fromDTO(topicDTO, topic);
            double startDuration = discipline.getDuration() == null ? 0 : discipline.getDuration();
            discipline.setDuration(startDuration+ topicDTO.getDuration());
            disciplineService.updateDiscipline(discipline);
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

    public byte[] getIcon(Long id) {
        try {
            TopicDTO topicDTO = topicConnector.getReferenceById(id);
            return topicDTO.getIcon();
        } catch (EntityNotFoundException e) {
            throw new TopicNotFoundException(id);
        }
    }
}
