package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TaskDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Discipline getById(Long id) {
        try {
            DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(id);
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
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
            group.setDiscipline(discipline);
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
}
