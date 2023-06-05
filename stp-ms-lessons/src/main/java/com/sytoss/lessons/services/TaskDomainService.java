package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.TaskDomainDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskDomainConnector taskDomainConnector;

    private final TaskDomainConvertor taskDomainConvertor;

    public TaskDomain create(TaskDomain taskDomain) {
        TaskDomainDTO taskDomainDTO = taskDomainConnector.getByName(taskDomain.getName());
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainConvertor.toDTO(taskDomain, taskDomainDTO);
            taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } else {
            throw new TaskDomainAlreadyExist(taskDomain.getName());
        }
    }

    public TaskDomain getById(Long taskDomainId) {
        try {
            TaskDomainDTO taskDomainDTO = taskDomainConnector.getReferenceById(taskDomainId);
            TaskDomain taskDomain = new TaskDomain();
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } catch (EntityNotFoundException exception) {
            throw new TaskDomainNotFoundException(taskDomainId);
        }
    }
}
