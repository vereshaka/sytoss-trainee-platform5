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
        List<TopicDTO> topicDTOS = source.getTopics().stream().toList();
        List<Topic> topics = new ArrayList<>();
        for (TopicDTO topicDTO : topicDTOS) {
            Topic tempTopic = new Topic();
            topicConvertor.fromDTO(topicDTO, tempTopic);
            topics.add(tempTopic);
        }
        destination.setTopics(topics);
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(source.getTaskDomainDTO(), taskDomain);
        destination.setTaskDomain(taskDomain);
    }

    public void toDTO(Task source, TaskDTO destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        List<TopicDTO> topicDTOS = new ArrayList<>();
        List<Topic> topics = source.getTopics();
        for (Topic topic : topics) {
            TopicDTO tempTopicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, tempTopicDTO);
            topicDTOS.add(tempTopicDTO);
        }
        destination.setTopics(topicDTOS);
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(source.getTaskDomain(), taskDomainDTO);
        destination.setTaskDomainDTO(taskDomainDTO);
    }
}