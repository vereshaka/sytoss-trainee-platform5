package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.TaskDomainDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskDomainConnector taskDomainConnector;

    private final TaskDomainConvertor taskDomainConvertor;

    private final DisciplineService disciplineService;

    private final PersonalExamConnector personalExamConnector;

    public TaskDomain create(Long disciplineId, TaskDomain taskDomain) {
        Discipline discipline = disciplineService.getById(disciplineId);
        TaskDomainDTO oldTaskDomainDTO = taskDomainConnector.getByNameAndDisciplineId(taskDomain.getName(), disciplineId);
        if (oldTaskDomainDTO == null) {
            taskDomain.setDiscipline(discipline);
            TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
            taskDomainConvertor.toDTO(taskDomain, taskDomainDTO);
            taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } else {
            throw new TaskDomainAlreadyExist(taskDomain.getName());
        }
    }

    public TaskDomain update(Long taskDomainId, TaskDomain taskDomain) {
        TaskDomain oldTaskDomain = getById(taskDomainId);
        List<PersonalExam> personalExams = personalExamConnector.findAllPersonalExamByTaskDomain(oldTaskDomain.getId());
        for (PersonalExam personalExam : personalExams) {
            for(Answer answer : personalExam.getAnswers()){
                if (answer.getTask().getTaskDomain().equals(oldTaskDomain) && !answer.getStatus().equals(AnswerStatus.ANSWERED)) {
                    throw new TaskDomainIsUsed(oldTaskDomain.getName());
                }
            }
        }
        oldTaskDomain.setName(taskDomain.getName());
        oldTaskDomain.setScript(taskDomain.getScript());
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
}
