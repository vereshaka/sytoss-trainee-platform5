package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.TeacherNotFoundException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.TeacherConnector;
import com.sytoss.lessons.convertors.TeacherConvertor;
import com.sytoss.lessons.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    public Teacher getById(Long id) {
        Optional<TeacherDTO> optionalTeacherDTO = teacherConnector.findById(id);
        TeacherDTO teacherDTO = optionalTeacherDTO.orElseThrow(() -> new TeacherNotFoundException(id));

        Teacher teacher = new Teacher();
        teacherConverter.fromDTO(teacherDTO, teacher);
        return teacher;
    }
}
