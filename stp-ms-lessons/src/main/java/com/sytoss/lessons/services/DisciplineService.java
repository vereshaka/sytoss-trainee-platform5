package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DisciplineService extends AbstractService {

    private final DisciplineConnector disciplineConnector;

    private final DisciplineConvertor disciplineConvertor;

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

    public Discipline create(Long teacherId, Discipline discipline) {
        DisciplineDTO oldDisciplineDTO = disciplineConnector.getByNameAndTeacherId(discipline.getName(), teacherId);
        if (oldDisciplineDTO == null) {
            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            discipline.setTeacher(teacher);
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
        List<DisciplineDTO> disciplineDTOList = disciplineConnector.findByTeacherId(getCurrentUser().getId());
        List<Discipline> result = new ArrayList<>();
        for (DisciplineDTO disciplineDTO : disciplineDTOList) {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            result.add(discipline);
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
}
