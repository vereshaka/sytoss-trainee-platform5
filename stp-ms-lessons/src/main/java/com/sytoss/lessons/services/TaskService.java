package com.sytoss.lessons.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.TaskConditionAlreadyExistException;
import com.sytoss.domain.bom.exceptions.business.TaskDontHasConditionException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import com.sytoss.lessons.connectors.CheckTaskConnector;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.TaskConditionConvertor;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskConnector taskConnector;

    private final TaskConditionService conditionService;

    private final TaskConvertor taskConvertor;

    private final TaskConditionConvertor taskConditionConvertor;

    private final TopicService topicService;

    private final CheckTaskConnector checkTaskConnector;

    private final TaskDomainConnector taskDomainConnector;

    private final PumlConvertor pumlConvertor;

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
        TaskDomainDTO taskDomainDTO = taskDomainConnector.getReferenceById(task.getTaskDomain().getId());
        TaskDTO taskDTO = new TaskDTO();
        taskConvertor.toDTO(task, taskDTO);
        taskDTO.setTaskDomain(taskDomainDTO);
        taskDTO = taskConnector.save(taskDTO);
        taskConvertor.fromDTO(taskDTO, task);
        return task;
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

    public List<Task> assignTasksToTopic(Long topicId, List<Long> taskIds) {
        Topic topic = topicService.getById(topicId);
        List<Task> result = new ArrayList<>();
        for (Long taskId : taskIds) {
            Task task = getById(taskId);
            task.setId(taskId);
            if (task.getTopics().isEmpty()) {
                task.setTopics(List.of(topic));
            } else {
                if (!task.getTopics().stream().map(Topic::getId).toList().contains(topicId)) {
                    task.getTopics().add(topic);
                }

            }
            TaskDTO taskDTO = new TaskDTO();
            taskConvertor.toDTO(task, taskDTO);
            taskDTO = taskConnector.save(taskDTO);
            taskConvertor.fromDTO(taskDTO, task);
            result.add(task);
        }
        return result;
    }

    public List<Task> findByTopicId(Long topicId) {
        List<TaskDTO> taskDTOList = taskConnector.findByTopicsIdAndDeleteDateIsNull(topicId);
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
        List<TaskDTO> taskDTOList = taskConnector.findByTaskDomainIdAndDeleteDateIsNull(taskDomainId);
        List<Task> result = new ArrayList<>();
        for (TaskDTO taskDTO : taskDTOList) {
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            result.add(task);
        }
        return result;
    }

    public QueryResult getQueryResult(TaskDomainRequestParameters taskDomainRequestParameters) {
        TaskDomainDTO taskDomainDTO = taskDomainConnector.getReferenceById(taskDomainRequestParameters.getTaskDomainId());
        if (taskDomainDTO != null) {
            String script = taskDomainDTO.getDatabaseScript() + "\n\n" + taskDomainDTO.getDataScript();
            String liquibaseScript = pumlConvertor.convertToLiquibase(script);
            CheckRequestParameters checkRequestParameters = new CheckRequestParameters();
            checkRequestParameters.setRequest(taskDomainRequestParameters.getRequest());
            checkRequestParameters.setScript(liquibaseScript);
            try {
                QueryResult queryResult = checkTaskConnector.checkRequest(checkRequestParameters);
                return queryResult;
            } catch (Exception e) {
                log.error("Error during check request", e);
                if (e instanceof FeignException) {
                    throw new RequestIsNotValidException(((FeignException) e).contentUTF8());
                }
            }
        }
        throw new TaskDomainNotFoundException(taskDomainRequestParameters.getTaskDomainId());

    }

    public Task updateTask(Task task) {
        log.debug("Updating Task with id [{}]", task.getId());

        Optional<TaskDTO> taskDTO = taskConnector.findById(task.getId());
        if (taskDTO.isEmpty()) {
            throw new TaskNotFoundException(task.getId());
        }

        TaskDTO updateTaskDTO = taskDTO.get();

        if(Objects.nonNull(task.getQuestion())) {
            updateTaskDTO.setQuestion(task.getQuestion());
        }

        if(Objects.nonNull(task.getEtalonAnswer())) {
            updateTaskDTO.setEtalonAnswer(task.getEtalonAnswer());
        }

        if(Objects.nonNull(task.getCoef())) {
            updateTaskDTO.setCoef(task.getCoef());
        }

        updateTaskDTO.setConditions(getTaskConditionsForUpdate(task, updateTaskDTO));

        updateTaskDTO = taskConnector.save(updateTaskDTO);
        taskConvertor.fromDTO(updateTaskDTO, task);

        return task;
    }

    public Task deleteTask(Long id) {
        TaskDTO taskDTO = taskConnector.getByIdAndDeleteDateIsNull(id);
        if(taskDTO != null) {
            taskDTO.setDeleteDate(new Date());
            taskConnector.save(taskDTO);
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            return task;
        }
        throw new TaskNotFoundException(id);
    }

    private List<TaskConditionDTO> getTaskConditionsForUpdate(Task task, TaskDTO taskDTO) {
        List<TaskConditionDTO> taskConditionDTOList = taskDTO.getConditions();

        for (int i = 0; i < task.getTaskConditions().size(); ++i) {
            taskConditionDTOList.get(i).setValue(task.getTaskConditions().get(i).getValue());
        }

        return taskConditionDTOList;
    }
}