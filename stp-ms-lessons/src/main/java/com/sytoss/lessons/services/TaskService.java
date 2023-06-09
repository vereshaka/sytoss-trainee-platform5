package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
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

    private final TaskConvertor taskConvertor;

    private final TopicService topicService;

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

    public Task assignTaskToTopic(Long taskId, Long topicId) {
        Topic topic = topicService.getById(topicId);
        Task task = getById(taskId);
        if(task.getTopics() == null){
            task.setTopics(List.of(topic));
        }else{
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
}
