package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.exceptions.business.TaskConditionAlreadyExistException;
import com.sytoss.domain.bom.exceptions.business.TaskDontHasConditionException;
import com.sytoss.domain.bom.exceptions.business.TaskExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.lessons.connectors.CheckTaskConnector;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskConnector taskConnector;

    private final TaskConditionService conditionService;

    private final TaskConvertor taskConvertor;

    private final TopicService topicService;

    private final CheckTaskConnector checkTaskConnector;

    public Task getById(Long id) {
        try {
            TaskDTO taskDTO = taskConnector.getReferenceById(id);
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            return task;
        } catch (EntityNotFoundException e) {
            throw new TaskNotFoundException(id);
        }
    }

    public Task create(Task task) {
        TaskDTO taskDTO = taskConnector.getByQuestionAndTaskDomainId(task.getQuestion(), task.getTaskDomain().getId());
        if (taskDTO == null) {
            taskDTO = new TaskDTO();
            taskConvertor.toDTO(task, taskDTO);
            taskDTO = taskConnector.save(taskDTO);
            taskConvertor.fromDTO(taskDTO, task);
            return task;
        }
        throw new TaskExistException(task.getQuestion());
    }

    public Task removeCondition(Long taskId, Long conditionId) {
        Task task = getById(taskId);
        TaskCondition taskCondition = conditionService.getById(conditionId);
        if (task.getTaskConditions().contains(taskCondition)) {
            task.getTaskConditions().remove(taskCondition);
            TaskDTO taskDTO = new TaskDTO();
            taskConvertor.toDTO(task, taskDTO);
            taskDTO = taskConnector.save(taskDTO);
            taskConvertor.fromDTO(taskDTO, task);
            return task;
        } else {
            throw new TaskDontHasConditionException(taskId, conditionId);
        }
    }

    public Task assignTaskToTopic(Long taskId, Long topicId) {
        Topic topic = topicService.getById(topicId);
        Task task = getById(taskId);
        if (task.getTopics().isEmpty()) {
            task.setTopics(List.of(topic));
        } else {
            task.getTopics().add(topic);
        }
        TaskDTO taskDTO = new TaskDTO();
        taskConvertor.toDTO(task, taskDTO);
        taskDTO = taskConnector.save(taskDTO);
        taskConvertor.fromDTO(taskDTO, task);
        return task;
    }

    public List<Task> findByTopicId(Long topicId) {
        List<TaskDTO> taskDTOList = taskConnector.findByTopicsId(topicId);
        List<Task> tasksList = new ArrayList<>();
        for (TaskDTO taskDTO : taskDTOList) {
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            tasksList.add(task);
        }
        return tasksList;
    }

    public Task addCondition(Long taskId, TaskCondition taskCondition) {
        Task result = getById(taskId);
        if (!result.getTaskConditions().contains(taskCondition)) {
            result.getTaskConditions().add(taskCondition);
            TaskDTO taskDTO = new TaskDTO();
            taskConvertor.toDTO(result, taskDTO);
            taskDTO = taskConnector.save(taskDTO);
            taskConvertor.fromDTO(taskDTO, result);
            return result;
        } else {
            throw new TaskConditionAlreadyExistException(taskCondition.getValue());
        }
    }

    public List<Task> findByDomainId(Long taskDomainId) {
        List<TaskDTO> taskDTOList = taskConnector.findByTaskDomainId(taskDomainId);
        List<Task> result = new ArrayList<>();
        for (TaskDTO taskDTO : taskDTOList) {
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            result.add(task);
        }
        return result;
    }

    public QueryResult getQueryResult(CheckRequestParameters checkRequestParameters) {
        return checkTaskConnector.checkRequest(checkRequestParameters);
    }
}