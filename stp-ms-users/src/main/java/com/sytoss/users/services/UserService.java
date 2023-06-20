package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.exceptions.LoadImageException;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends AbstractService {

    private final UserConnector userConnector;

    private final UserConverter userConverter;

    public AbstractUser getById(Long userId) {
        UserDTO foundUser = userConnector.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return instantiateUser(foundUser);
    }

    protected UserDTO getMeAsDto() {
        String email = getMyEmail();
        UserDTO foundUser = userConnector.getByEmail(email);
        if (foundUser == null) {
            throw new UserNotFoundException("Unknown user:" + email);
        }
        return foundUser;
    }

    @Transactional
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
            userConverter.fromDTO(userDto, result);
        } else if (userDto instanceof StudentDTO) {
            result = new Student();
            userConverter.fromDTO(userDto, result);
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
            userConverter.fromDTO(getJwt(), newUser);
            TeacherDTO userDto = new TeacherDTO();
            userConverter.toDTO(newUser, userDto);
            userConnector.save(userDto);

        } else if (userType.equalsIgnoreCase("student")) {
            Student newUser = new Student();
            userConverter.fromDTO(getJwt(), newUser);
            StudentDTO userDto = new StudentDTO();
            userConverter.toDTO(newUser, userDto);
            userConnector.save(userDto);
        } else {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
        log.info("New " + userType + " created. email:" + email);
    }

    public void updatePhoto(MultipartFile photo) {
        UserDTO dto = getMeAsDto();
        try {
            dto.setPhoto(photo.getBytes());
        } catch (IOException e) {
            throw new LoadImageException("Could not read bytes of photo for user " + getMyEmail(), e);
        }
        dto.setModerated(false);
        userConnector.save(dto);
    }

    @Transactional
    public void updateProfile(ProfileModel profileModel) {
        UserDTO dto = getMeAsDto();
        dto.setFirstName(profileModel.getFirstName());
        dto.setLastName(profileModel.getLastName());
        if ((dto instanceof StudentDTO) && profileModel.getPrimaryGroup() != null) {
            //TODO: yevgenyv: update group info
        }
        userConnector.save(dto);
    }
}