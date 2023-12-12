package com.sytoss.lessons.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.EtalonIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.ScriptIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskDomainService {

    private final TaskDomainConnector taskDomainConnector;

    private final TaskDomainConvertor taskDomainConvertor;

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final TaskService taskService;

    private final PumlConvertor pumlConvertor;

    private final DisciplineConnector disciplineConnector;

    private final ExamService examService;

    private final ImageProviderConnector imageProviderConnector;

    public TaskDomain create(Long disciplineId, TaskDomain taskDomain) {
        DisciplineDTO disciplineDTO = disciplineConnector.findById(disciplineId).orElseThrow(() -> new DisciplineNotFoundException(disciplineId));
        TaskDomainDTO oldTaskDomainDTO = taskDomainConnector.getByNameAndDisciplineId(taskDomain.getName(), disciplineId);
        if (oldTaskDomainDTO == null) {
            TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
            taskDomainConvertor.toDTO(taskDomain, taskDomainDTO);
            taskDomainDTO.setDiscipline(disciplineDTO);
            if (!isValid(taskDomain)) {
                throw new ScriptIsNotValidException("Script is not valid");
            }
            generateOrUpdateImage(taskDomain, taskDomainDTO);
            taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } else {
            throw new TaskDomainAlreadyExist(taskDomain.getName());
        }
    }

    public TaskDomain update(Long taskDomainId, TaskDomain taskDomain) {
        TaskDomain oldTaskDomain = getById(taskDomainId);
        boolean isUsed = personalExamConnector.taskDomainIsUsed(oldTaskDomain.getId());
        if (isUsed) {
            throw new TaskDomainIsUsed(oldTaskDomain.getName());
        }
        List<Task> tasks = taskService.findByDomainId(oldTaskDomain.getId());
        for (Task task : tasks) {
            CheckRequestParameters input = new CheckRequestParameters();
            input.setRequest(task.getEtalonAnswer());
            input.setScript(taskDomain.getDatabaseScript() + StringUtils.LF + taskDomain.getDataScript());
            IsCheckEtalon isCheckEtalon = checkTaskConnector.checkEtalon(input);
            if (!isCheckEtalon.isChecked()) {
                throw new EtalonIsNotValidException("etalon isn't correct for task with question \"" + task.getQuestion()
                        + "\"\nWith message exception\n" + isCheckEtalon.getException());
            }
        }
        oldTaskDomain.setName(taskDomain.getName());
        oldTaskDomain.setDatabaseScript(taskDomain.getDatabaseScript());
        oldTaskDomain.setDataScript(taskDomain.getDataScript());
        if (!isValid(oldTaskDomain)) {
            throw new ScriptIsNotValidException("Script is not valid");
        }
        oldTaskDomain.setDiscipline(taskDomain.getDiscipline());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(oldTaskDomain, taskDomainDTO);
        taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
        taskDomainConvertor.fromDTO(taskDomainDTO, oldTaskDomain);
        return oldTaskDomain;
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

    public List<TaskDomain> findByDiscipline(Long disciplineId) {
        List<TaskDomainDTO> taskDomainDTOList = taskDomainConnector.findByDisciplineId(disciplineId);
        List<TaskDomain> result = new ArrayList<>();
        for (TaskDomainDTO taskDomainDTO : taskDomainDTOList) {
            TaskDomain taskDomain = new TaskDomain();
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            result.add(taskDomain);
        }
        return result;
    }

    public byte[] generatePngFromPuml(String puml, ConvertToPumlParameters convertToPumlParameters) {
        return pumlConvertor.generatePngFromPuml(puml, convertToPumlParameters);
    }

    public TaskDomainModel getCountOfTasks(Long taskDomainId) {
        List<Task> tasks = taskService.findByDomainId(taskDomainId);
        TaskDomainModel taskDomainModel = new TaskDomainModel();
        taskDomainModel.setCountOfTasks(tasks.size());
        return taskDomainModel;
    }

    public List<Task> getTasks(Long taskDomainId) {
        return taskService.findByDomainId(taskDomainId);
    }

    public TaskDomain updateById(TaskDomain taskDomain) {
        TaskDomain taskDomainToUpdate = getById(taskDomain.getId());
        taskDomainToUpdate.setName(taskDomain.getName());
        taskDomainToUpdate.setShortDescription(taskDomain.getShortDescription());
        taskDomainToUpdate.setDatabaseScript(taskDomain.getDatabaseScript());
        taskDomainToUpdate.setDataScript(taskDomain.getDataScript());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(taskDomainToUpdate, taskDomainDTO);
        if (!isValid(taskDomainToUpdate)) {
            throw new ScriptIsNotValidException("Script is not valid");
        }
        generateOrUpdateImage(taskDomain, taskDomainDTO);
        taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
        taskDomainConvertor.fromDTO(taskDomainDTO, taskDomainToUpdate);
        return taskDomainToUpdate;
    }

    private void generateOrUpdateImage(TaskDomain taskDomain, TaskDomainDTO taskDomainDTO) {
        try {
            String puml = taskDomain.getDatabaseScript() + "\n\n" + taskDomain.getDataScript();
            byte[] dbImage = generatePngFromPuml(puml, ConvertToPumlParameters.DB);
            byte[] dataImage = generatePngFromPuml(puml, ConvertToPumlParameters.DATA);
            if (Objects.isNull(taskDomainDTO.getDbImageName())) {
                taskDomainDTO.setDbImageName(imageProviderConnector.saveImage(dbImage));
            } else {
                imageProviderConnector.saveImageByteWithName(taskDomainDTO.getDbImageName(), dbImage);
            }
            if (Objects.isNull(taskDomainDTO.getDataImageName())) {
                taskDomainDTO.setDataImageName(imageProviderConnector.saveImage(dataImage));
            } else {
                imageProviderConnector.saveImageByteWithName(taskDomainDTO.getDataImageName(), dataImage);
            }
        } catch (Exception exception) {
            log.warn("Could not update or generate image for task domain: {}", exception.getMessage());
        }
    }

    private boolean isValid(TaskDomain taskDomain) {
        String liquibaseScript = pumlConvertor.convertToLiquibase(taskDomain.getDatabaseScript() + "\n\n" + taskDomain.getDataScript());
        return checkTaskConnector.checkValidation(liquibaseScript);
    }

    public TaskDomain delete(Long taskDomainId) {
        TaskDomainDTO taskDomain = taskDomainConnector.getReferenceById(taskDomainId);
        List<Task> tasks = taskService.findByDomainId(taskDomainId);

        if (Objects.nonNull(tasks)) {
            tasks.forEach(task -> {
                taskService.deleteTask(task.getId());
            });
        }

        taskDomainConnector.delete(taskDomain);
        TaskDomain taskDomainDeleted = new TaskDomain();
        taskDomainConvertor.fromDTO(taskDomain, taskDomainDeleted);

        return taskDomainDeleted;
    }

    public List<Exam> getExams(Long taskDomainId) {
        return examService.getExamsByTaskDomainId(taskDomainId);
    }

    public String getDbImage(Long taskDomainId) {
        TaskDomainDTO taskDomainDTO = taskDomainConnector.findById(taskDomainId).orElseThrow(() -> new TaskDomainNotFoundException(taskDomainId));
        return taskDomainDTO.getDbImageName();
    }

    public String getDataImage(Long taskDomainId) {
        TaskDomainDTO taskDomainDTO = taskDomainConnector.findById(taskDomainId).orElseThrow(() -> new TaskDomainNotFoundException(taskDomainId));
        return taskDomainDTO.getDataImageName();
    }
}
