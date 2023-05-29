package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.businessException.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class DisciplineService {

    private final DisciplineConnector disciplineConnector;

    private final DisciplineConvertor disciplineConvertor;

    private final TeacherService teacherService;

    public Discipline getById(Long id) {
        DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(id);
        if (disciplineDTO != null) {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            return discipline;
        } else {
            throw new DisciplineNotFoundException(id);
        }
    }

    public Discipline create(Long teacherId, Discipline discipline) {
        DisciplineDTO oldDisciplineDTO = disciplineConnector.getByNameAndTeacherId(discipline.getName(), teacherId);
        Teacher teacher = teacherService.getById(teacherId);
        if (oldDisciplineDTO == null) {
            Discipline newDiscipline = new Discipline();
            newDiscipline.setName(discipline.getName());
            newDiscipline.setTeacher(teacher);
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            disciplineConvertor.toDTO(newDiscipline, disciplineDTO);
            disciplineDTO = disciplineConnector.saveAndFlush(disciplineDTO);
            disciplineConvertor.fromDTO(disciplineDTO, newDiscipline);
            return newDiscipline;
        } else {
            throw new DisciplineExistException(discipline.getName());
        }
    }
}
