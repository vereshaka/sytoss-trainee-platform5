package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class DisciplineService extends AbstractService {

    private final DisciplineConnector disciplineConnector;

    private final DisciplineConvertor disciplineConvertor;

    private final TaskConvertor taskConvertor;

    private final GroupReferenceConnector groupReferenceConnector;

    private final UserConnector userConnector;

    private final TaskConnector taskConnector;

    private final ExamService examService;

    private final TopicConnector topicConnector;

    private final TaskDomainConnector taskDomainConnector;

    public Discipline getById(Long id) {
        try {
            DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(id);
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            discipline.setDuration(topicConnector.countDurationByDisciplineId(discipline.getId()));
            return discipline;
        } catch (EntityNotFoundException e) {
            throw new DisciplineNotFoundException(id);
        }
    }

    public Discipline create(Discipline discipline) {
        DisciplineDTO oldDisciplineDTO = disciplineConnector.getByNameAndTeacherId(discipline.getName(), getCurrentUser().getId());
        if (oldDisciplineDTO == null) {
            Teacher teacher = new Teacher();
            teacher.setId(getCurrentUser().getId());
            discipline.setTeacher(teacher);
            discipline.setCreationDate(Timestamp.from(Instant.now()));
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            disciplineConvertor.toDTO(discipline, disciplineDTO);
            disciplineDTO = disciplineConnector.saveAndFlush(disciplineDTO);
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            return discipline;
        } else {
            throw new DisciplineExistException(discipline.getName());
        }
    }

    public List<Discipline> findDisciplines() {
        List<DisciplineDTO> disciplineDTOList = disciplineConnector.findByTeacherIdOrderByCreationDateDesc(getCurrentUser().getId());
        List<Discipline> result = new ArrayList<>();
        for (DisciplineDTO disciplineDTO : disciplineDTOList) {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            result.add(discipline);
        }
        return result;
    }

    public List<Task> findTasksByDisciplineId(Long id) {
        List<TaskDTO> tasks = taskConnector.getByTaskDomainDisciplineId(id);
        List<Task> result = new ArrayList<>();
        for (TaskDTO taskDTO : tasks) {
            Task task = new Task();
            taskConvertor.fromDTO(taskDTO, task);
            result.add(task);
        }
        result.sort(Comparator.comparing(task -> task.getTaskDomain().getName()));
        return result;
    }

    public List<Discipline> findAllDisciplines() {
        List<DisciplineDTO> disciplineDTOS = disciplineConnector.findAll();
        List<Discipline> disciplines = new ArrayList<>();
        for (DisciplineDTO disciplineDTO : disciplineDTOS) {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            disciplines.add(discipline);
        }
        return disciplines;
    }

    public void assignGroupToDiscipline(Long disciplineId, Long groupId) {
        getById(disciplineId);
        DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(disciplineId);
        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO);
        groupReferenceConnector.save(groupReferenceDTO);
    }

    public List<Group> getGroups(Long disciplineId) {

        List<GroupReferenceDTO> groups = groupReferenceConnector.findByDisciplineId(disciplineId);
        List<Group> result = new ArrayList<>();
        Discipline discipline = getById(disciplineId);
        for (GroupReferenceDTO item : groups) {
            Group group = new Group();
            group.setId(item.getGroupId());
            group.setDisciplines(List.of(discipline));
            result.add(group);
        }
        return result;
    }

    public byte[] getIcon(Long id) {
        try {
            DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(id);
            return disciplineDTO.getIcon();
        } catch (EntityNotFoundException e) {
            throw new DisciplineNotFoundException(id);
        }
    }

    public List<Discipline> findAllMyDiscipline() {
        List<Discipline> disciplines = new ArrayList<>();
        List<Long> groupsId = userConnector.findMyGroupId();
        for (Long groupId : groupsId) {
            List<DisciplineDTO> disciplineDTOList = disciplineConnector.findByGroupReferencesGroupId(groupId);
            for (DisciplineDTO disciplineDTO : disciplineDTOList) {
                Discipline discipline = new Discipline();
                disciplineConvertor.fromDTO(disciplineDTO, discipline);
                disciplines.add(discipline);
            }
        }
        return disciplines;
    }

    public void assignGroupsToDiscipline(Long disciplineId, List<Long> groupsIds) {
        for (Long id : groupsIds) {
            assignGroupToDiscipline(disciplineId, id);
        }
    }

    public Discipline updateDiscipline(Discipline discipline) {
        DisciplineDTO updatedDisciplineDTO = disciplineConnector.getReferenceById(discipline.getId());
        if (updatedDisciplineDTO.equals(null)) {
            throw new DisciplineNotFoundException(discipline.getId());
        }
        if (!Objects.equals(discipline.getName(), null)) {
            updatedDisciplineDTO.setName(discipline.getName());
        }
        if (!Objects.equals(discipline.getShortDescription(), null)) {
            updatedDisciplineDTO.setShortDescription(discipline.getShortDescription());
        }
        if (!Objects.equals(discipline.getFullDescription(), null)) {
            updatedDisciplineDTO.setFullDescription(discipline.getFullDescription());
        }
        if (!Objects.equals(discipline.getIcon(), null)) {
            updatedDisciplineDTO.setIcon(discipline.getIcon());
        }
        disciplineConnector.save(updatedDisciplineDTO);
        Discipline updatedDiscipline = new Discipline();
        disciplineConvertor.fromDTO(updatedDisciplineDTO, updatedDiscipline);
        return updatedDiscipline;
    }

    public List<Exam> getExams(Long disciplineId) {
        return examService.getExamsByDiscipline(disciplineId);
    }

    public Discipline delete(Long disciplineId) {
        Discipline discipline = getById(disciplineId);
        List<TopicDTO> topicDTOList = topicConnector.findByDisciplineId(disciplineId);
        List<TaskDomainDTO> taskDomainDTOList = taskDomainConnector.findByDisciplineId(disciplineId);
        List<Exam> examList = examService.getExamsByDiscipline(disciplineId);

        taskDomainDTOList.forEach(taskDomainDTO -> {
            List<TaskDTO> taskDTOList = taskConnector.findByTaskDomainIdOrderByCodeAscCreateDateDesc(taskDomainDTO.getId());
            taskDTOList.forEach(examService::deleteAssignTaskToExam);
            taskConnector.deleteAll(taskDTOList);
        });

        taskDomainConnector.deleteAll(taskDomainDTOList);

        topicConnector.deleteAll(topicDTOList);

        examList.forEach(exam -> examService.delete(exam.getId()));

        disciplineConnector.deleteById(disciplineId);
        return discipline;
    }

    public List<Discipline> findDisciplinesByGroupId(Long groupId) {
        List<DisciplineDTO> disciplineDTOS = disciplineConnector.findByGroupReferencesGroupId(groupId);
        return disciplineDTOS.stream().map(disciplineDTO -> {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            return discipline;
        }).collect(Collectors.toList());
    }
}
