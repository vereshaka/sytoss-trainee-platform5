package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.StudentConvertor;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends AbstractService {

    private final TeacherConnector teacherConnector;

    private final TeacherConvertor teacherConverter;

    private final StudentConnector studentConnector;

    private final StudentConvertor studentConvertor;

    public Teacher getById(Long teacherId) {
        TeacherDTO teacherDTO = teacherConnector.getReferenceById(teacherId);
        Teacher result = new Teacher();
        teacherConverter.fromDTO(teacherDTO, result);
        return result;
    }

    public AbstractUser getOrCreateUser(String email) {
        AbstractUser result = null;
        if (getJwt().getClaim("userType").toString().equalsIgnoreCase("teacher")) {
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

            Teacher teacher = new Teacher();
            teacherConverter.fromDTO(teacherDTO, teacher);
            result = teacher;
        }

        if (getJwt().getClaim("userType").toString().equalsIgnoreCase("student")) {
            StudentDTO studentDTO = studentConnector.findByEmail(email);

            if (studentDTO == null) {
                Student student = new Student();
                student.setEmail(email);
                student.setFirstName(getJwt().getClaim("firstName"));
                student.setLastName(getJwt().getClaim("lastName"));
                student.setMiddleName(getJwt().getClaim("middleName"));
                studentDTO = new StudentDTO();
                studentConvertor.toDTO(student, studentDTO);
                studentDTO = studentConnector.save(studentDTO);
            }

            Student student = new Student();
            studentConvertor.fromDTO(studentDTO, student);
            result = student;
        }

        result.setVerified(false);
        return result;
    }
}
