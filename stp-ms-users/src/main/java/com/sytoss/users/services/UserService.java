package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.StudentConvertor;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends AbstractService {

    private final UserConnector userConnector;

    private final TeacherConvertor teacherConverter;

    private final StudentConvertor studentConvertor;

    public AbstractUser getById(Long userId) {
        UserDTO foundUser = userConnector.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return instantiateUser(foundUser);
    }

    public AbstractUser getOrCreateUser(String email) {
        UserDTO userDto = userConnector.getByEmail(email);
        if (userDto == null) {
            registerUser(email);
            userDto = userConnector.getByEmail(email);
        }
        return instantiateUser(userDto);
    }

    private AbstractUser instantiateUser(UserDTO userDto) {
        AbstractUser result = null;
        if (userDto instanceof TeacherDTO) {
            result = new Teacher();
            teacherConverter.fromDTO(userDto, result);
        } else if (userDto instanceof StudentDTO) {
            result = new Student();
            studentConvertor.fromDTO(userDto, result);
        } else {
            throw new IllegalArgumentException("Unsupported user class: " + userDto.getClass());
        }
        return result;
    }

    private void registerUser(String email) {
        String userType = getJwt().getClaim("userType") != null ? getJwt().getClaim("userType").toString().toLowerCase() : "unknown";
        log.info("No user found for " + email + ". Start creation " + userType + " based on token info...");
        if (userType.equalsIgnoreCase("teacher")) {
            Teacher newUser = new Teacher();
            teacherConverter.fromDTO(getJwt(), newUser);
            TeacherDTO userDto = new TeacherDTO();
            teacherConverter.toDTO(newUser, userDto);
            userConnector.save(userDto);

        } else if (userType.equalsIgnoreCase("student")) {
            Student newUser = new Student();
            studentConvertor.fromDTO(getJwt(), newUser);
            StudentDTO userDto = new StudentDTO();
            studentConvertor.toDTO(newUser, userDto);
            userConnector.save(userDto);
        } else {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
        log.info("New " + userType + " created. email:" + email);
    }
}
