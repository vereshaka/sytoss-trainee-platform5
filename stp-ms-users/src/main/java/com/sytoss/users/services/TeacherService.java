package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService extends AbstractService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    public Teacher getById(Long teacherId) {
        TeacherDTO teacherDTO = teacherConnector.getReferenceById(teacherId);
        Teacher result = new Teacher();
        teacherConverter.fromDTO(teacherDTO, result);
        return result;
    }

    public AbstractUser getOrCreateUser(String email) {
        TeacherDTO teacherDTO = teacherConnector.getByEmail(email);
        if (teacherDTO == null) {
            log.info("No user found for " + email + ". Start creation based on token info...");
            Teacher teacher = new Teacher();
            teacher.setEmail(email);
            teacher.setFirstName(getJwt().getClaim("firstName"));
            teacher.setLastName(getJwt().getClaim("lastName"));
            teacher.setMiddleName(getJwt().getClaim("middleName"));
            teacherDTO = new TeacherDTO();
            teacherConverter.toDTO(teacher, teacherDTO);
            teacherDTO = teacherConnector.save(teacherDTO);
            log.info("New user created. id: " + teacherDTO.getId() + ", email:" + email);
        }
        Teacher result = new Teacher();
        teacherConverter.fromDTO(teacherDTO, result);
        return result;
    }
}
