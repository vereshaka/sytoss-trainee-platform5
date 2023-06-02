package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskConnector taskConnector;

    private final TaskConvertor taskConvertor;

    public List<Task> findByTopicId(Long topicId) {
        List<TaskDTO> taskDTOList = taskConnector.findTasksByTopicsId(topicId);
        if(!taskDTOList.isEmpty()) {
            List<Task> tasksList = new ArrayList<>();
            for (TaskDTO taskDTO : taskDTOList) {
                Task task = new Task();
                taskConvertor.fromDTO(taskDTO, task);
                tasksList.add(task);
            }
            return tasksList;
        } else {
            throw new TaskNotFoundException(topicId);
        }
    }
}
