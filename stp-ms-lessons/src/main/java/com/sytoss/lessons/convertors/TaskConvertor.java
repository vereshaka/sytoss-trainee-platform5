package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskConvertor {

    private final TaskDomainConvertor taskDomainConvertor;

    private final TopicConvertor topicConvertor;

    public void fromDTO(TaskDTO source, Task destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(source.getTaskDomain(), taskDomain);
        destination.setTaskDomain(taskDomain);
        List<Topic> topicList = new ArrayList<>();
        if (source.getTopics() != null) {
            source.getTopics().forEach(topicDTO -> {
                Topic topic = new Topic();
                topicConvertor.fromDTO(topicDTO, topic);
                topicList.add(topic);
            });
        }
        destination.setTopics(topicList);
    }

    public void toDTO(Task source, TaskDTO destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(source.getTaskDomain(), taskDomainDTO);
        destination.setTaskDomain(taskDomainDTO);
        List<TopicDTO> topicDTOList = new ArrayList<>();
        if (source.getTopics() != null) {
            source.getTopics().forEach(topic -> {
                TopicDTO topicDTO = new TopicDTO();
                topicConvertor.toDTO(topic, topicDTO);
                topicDTOList.add(topicDTO);
            });
        }
        destination.setTopics(topicDTOList);
    }
}
