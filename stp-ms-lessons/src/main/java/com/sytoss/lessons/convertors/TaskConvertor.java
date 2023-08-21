package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.TaskConditionDTO;
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

    private final TaskConditionConvertor taskConditionConvertor;

    private final TopicConvertor topicConvertor;

    public void fromDTO(TaskDTO source, Task destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        destination.setCoef(source.getCoef());
        destination.setRequiredCommand(source.getRequiredCommand());
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(source.getTaskDomain(), taskDomain);
        destination.setTaskDomain(taskDomain);
        if (!source.getConditions().isEmpty()) {
            List<TaskCondition> taskConditionList = new ArrayList<>();
            source.getConditions().forEach(taskConditionDTO -> {
                TaskCondition taskCondition = new TaskCondition();
                taskConditionConvertor.fromDTO(taskConditionDTO, taskCondition);
                taskConditionList.add(taskCondition);
            });
            destination.setTaskConditions(taskConditionList);
        }
        if (source.getTopics() != null) {
            List<Topic> topicList = new ArrayList<>();
            source.getTopics().forEach(topicDTO -> {
                Topic topic = new Topic();
                topicConvertor.fromDTO(topicDTO, topic);
                topicList.add(topic);
            });
            destination.setTopics(topicList);
        }
    }

    public void toDTO(Task source, TaskDTO destination) {
        destination.setId(source.getId());
        destination.setQuestion(source.getQuestion());
        destination.setEtalonAnswer(source.getEtalonAnswer());
        destination.setCoef(source.getCoef());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(source.getTaskDomain(), taskDomainDTO);
        destination.setTaskDomain(taskDomainDTO);
        if (!source.getTaskConditions().isEmpty()) {
            List<TaskConditionDTO> taskConditionDTOList = new ArrayList<>();
            source.getTaskConditions().forEach(taskCondition -> {
                TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
                taskConditionConvertor.toDTO(taskCondition, taskConditionDTO);
                taskConditionDTOList.add(taskConditionDTO);
            });
            destination.setConditions(taskConditionDTOList);
        }
        if (source.getTopics() != null) {
            List<TopicDTO> topicDTOList = new ArrayList<>();
            source.getTopics().forEach(topic -> {
                TopicDTO topicDTO = new TopicDTO();
                topicConvertor.toDTO(topic, topicDTO);
                topicDTOList.add(topicDTO);
            });
            destination.setTopics(topicDTOList);
        }
    }
}
