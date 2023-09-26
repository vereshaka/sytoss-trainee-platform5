package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
        destination.setCode(source.getCode() == null ? "" : source.getCode());
        destination.setCreateDate(source.getCreateDate());
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(source.getTaskDomain(), taskDomain);
        destination.setTaskDomain(taskDomain);

        if (source.getConditions().isEmpty()) {
            destination.setRequiredCommand("");
            destination.setTaskConditions(new ArrayList<>());
        } else {
            List<TaskCondition> taskConditionList = new ArrayList<>();
            source.getConditions().forEach(taskConditionDTO -> {
                TaskCondition taskCondition = new TaskCondition();
                taskConditionConvertor.fromDTO(taskConditionDTO, taskCondition);
                taskConditionList.add(taskCondition);
            });
            destination.setTaskConditions(taskConditionList);

            taskConditionList.forEach(el -> {
                if (el.getType().equals(ConditionType.NOT_CONTAINS)) {
                    el.setValue("!" + el.getValue());
                }
            });
            destination.setRequiredCommand(String.join(",", taskConditionList.stream().map(TaskCondition::getValue).toList()));
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
        destination.setCode(source.getCode());
        destination.setCreateDate(source.getCreateDate());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(source.getTaskDomain(), taskDomainDTO);
        destination.setTaskDomain(taskDomainDTO);

        if (source.getRequiredCommand() != null && !source.getRequiredCommand().equals("")) {
            List<TaskCondition> taskConditions = new ArrayList<>();
            fromRequiredCommandToTaskConditions(source.getRequiredCommand(), taskConditions);
            if (!taskConditions.isEmpty()) {
                List<TaskConditionDTO> taskConditionDTOS = new ArrayList<>();
                taskConditions.forEach(taskCondition -> {
                    TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
                    taskConditionConvertor.toDTO(taskCondition, taskConditionDTO);
                    taskConditionDTOS.add(taskConditionDTO);
                });
                destination.setConditions(taskConditionDTOS);
            }
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

    public void fromRequiredCommandToTaskConditions(Task task) {
        if (task.getRequiredCommand() != null) {
            if(!task.getRequiredCommand().equals("")){
                List<TaskCondition> taskConditions = new ArrayList<>();
                task.setTaskConditions(fromRequiredCommandToTaskConditions(task.getRequiredCommand(), taskConditions));
            }
        }
    }

    private List<TaskCondition> fromRequiredCommandToTaskConditions(String requiredCommand, List<TaskCondition> taskConditions) {
        if (taskConditions == null) {
            taskConditions = new ArrayList<>();
        }
        List<String> commands = Arrays.stream(requiredCommand.split(",")).map(String::trim).toList();
        for (String command : commands) {
            TaskCondition taskCondition = new TaskCondition();
            if (command.contains("!")) {
                taskCondition.setType(ConditionType.NOT_CONTAINS);
            } else {
                taskCondition.setType(ConditionType.CONTAINS);
            }
            taskCondition.setValue(command.replaceAll("!", ""));
            taskConditions.add(taskCondition);
        }
        return taskConditions;
    }
}
