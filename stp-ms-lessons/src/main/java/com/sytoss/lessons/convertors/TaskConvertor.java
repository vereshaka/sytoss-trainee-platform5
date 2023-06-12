package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
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

    private final TopicConvertor topicConvertor;

    public void fromDTO(TaskDTO source, Task destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        List<Topic> topicList = new ArrayList<>();
        source.getTopics().forEach(topicDTO -> {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        });
        destination.setTopics(topicList);
    }

    public void toDTO(Task source, TaskDTO destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        List<TopicDTO> topicDTOList = new ArrayList<>();
        source.getTopics().forEach(topic -> {
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            topicDTOList.add(topicDTO);
        });
        destination.setTopics(topicDTOList);
    }
}
