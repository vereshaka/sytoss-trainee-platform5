package com.sytoss.users.services;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    public Teacher create(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherConverter.toDTO(teacher, teacherDTO);
        teacherDTO = teacherConnector.save(teacherDTO);
        teacherConverter.fromDTO(teacherDTO, teacher);
        return teacher;
    }
}
