package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TeacherNotFoundException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.TeacherConnector;
import com.sytoss.lessons.convertors.TeacherConvertor;
import com.sytoss.lessons.dto.TeacherDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    public Teacher getById(Long id) {
        try {
            TeacherDTO teacherDTO = teacherConnector.getReferenceById(id);
            Teacher teacher = new Teacher();
            teacherConverter.fromDTO(teacherDTO, teacher);
            return teacher;
        } catch (EntityNotFoundException e) {
            throw new TeacherNotFoundException(id);
        }
    }
}
