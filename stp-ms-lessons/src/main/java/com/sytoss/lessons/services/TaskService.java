package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskDontHasConditionException;
import com.sytoss.domain.bom.exceptions.business.TaskExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskConnector taskConnector;

    private final TaskConditionService conditionService;

    private final TaskConvertor taskConvertor;

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
        TaskDTO taskDTO = taskConnector.getByQuestionAndTopicsDisciplineId(task.getQuestion(), task.getTopics().get(0).getId());
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
            Iterator<TaskCondition> taskConditionIterator = task.getTaskConditions().iterator();
            while(taskConditionIterator.hasNext()){
                TaskCondition condition =taskConditionIterator.next();
                if(condition.getName().equals(taskCondition.getName())){
                    taskConditionIterator.remove();
                }
            }
            TaskDTO taskDTO = new TaskDTO();
            taskConvertor.toDTO(task, taskDTO);
            taskDTO = taskConnector.save(taskDTO);
            taskConvertor.fromDTO(taskDTO, task);
            return task;
        } else {
            throw new TaskDontHasConditionException(taskId, conditionId);
        }
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
}
