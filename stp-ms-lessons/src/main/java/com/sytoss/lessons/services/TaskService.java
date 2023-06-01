package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TeacherNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.connectors.TeacherConnector;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.convertors.TeacherConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskConnector taskConnector;

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
}
