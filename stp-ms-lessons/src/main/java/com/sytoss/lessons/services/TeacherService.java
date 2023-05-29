package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.TeacherNotFoundException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.TeacherConnector;
import com.sytoss.lessons.convertors.TeacherConvertor;
import com.sytoss.lessons.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    public Teacher getById(Long id) {
        TeacherDTO teacherDTO = teacherConnector.getReferenceById(id);
        if (teacherDTO != null) {
            Teacher teacher = new Teacher();
            teacherConverter.fromDTO(teacherDTO, teacher);
            return teacher;
        } else {
            throw new TeacherNotFoundException(id);
        }
    }

}
