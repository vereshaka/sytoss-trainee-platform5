package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskConditionNotFoundException;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.connectors.TaskConditionConnector;
import com.sytoss.lessons.convertors.TaskConditionConvertor;
import com.sytoss.lessons.dto.TaskConditionDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaskConditionService {

    private final TaskConditionConnector taskConditionConnector;

    private final TaskConditionConvertor taskConditionConvertor;

    public TaskCondition getById(Long id) {
        try {
            TaskConditionDTO taskConditionDTO = taskConditionConnector.getReferenceById(id);
            TaskCondition taskCondition = new TaskCondition();
            taskConditionConvertor.fromDTO(taskConditionDTO, taskCondition);
            return taskCondition;
        } catch (EntityNotFoundException e) {
            throw new TaskConditionNotFoundException(id);
        }
    }

    public void delete(TaskConditionDTO taskConditionDTO) {
        try {
            taskConditionConnector.delete(taskConditionDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public TaskConditionDTO addCondition(TaskConditionDTO taskConditionDTO) {
        return taskConditionConnector.save(taskConditionDTO);
    }
}
