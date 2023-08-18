package com.sytoss.users.services;

import com.sytoss.common.AbstractStpService;
import com.sytoss.domain.bom.exceptions.business.LoadImageException;
import com.sytoss.domain.bom.exceptions.business.NotAllowedTeacherRegistrationException;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.controllers.GroupReferenceConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.*;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import com.sytoss.users.services.exceptions.UserPhotoNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends AbstractStpService {

    private final UserConnector userConnector;

    private final UserConverter userConverter;

    private final GroupConvertor groupConvertor;

    private final GroupReferenceConnector groupReferenceConnector;

    private final GroupConnector groupConnector;

    @Value("${registration.allow-registration}")
    private String isAllowed;

    public AbstractUser getById(String userId) {
        UserDTO foundUser = getDTOById(userId);
        return instantiateUser(foundUser);
    }

    private UserDTO getDTOById(String userId) {
        try {
            return userConnector.getByUid(userId);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("User not found", e);
        }
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
        AbstractUser result;
        if (userDto instanceof TeacherDTO) {
            Teacher teacher = new Teacher();
            userConverter.fromDTO(userDto, teacher);
            result = teacher;
        } else if (userDto instanceof StudentDTO) {
            Student student = new Student();
            userConverter.fromDTO((StudentDTO) userDto, student);
            result = student;
        } else {
            throw new IllegalArgumentException("Unsupported user class: " + userDto.getClass());
        }
        return result;
    }

    private void registerUser(String email) {
        String userType = getJwt().getClaim("userType") != null ? getJwt().getClaim("userType").toString().toLowerCase() : "unknown";
        log.info("No user found for " + email + ". Start creation " + userType + " based on token info...");
        if (userType.equalsIgnoreCase("teacher")) {
            if (isAllowed.equals("false")) {
                throw new NotAllowedTeacherRegistrationException("Registration teacher isn't allowed");
            }
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
        if (profileModel.getFirstName() != null) {
            dto.setFirstName(profileModel.getFirstName());
        }
        if (profileModel.getMiddleName() != null) {
            dto.setMiddleName(profileModel.getMiddleName());
        }
        if (profileModel.getLastName() != null) {
            dto.setLastName(profileModel.getLastName());
        }
        if (profileModel.getPhoto() != null) {
            updatePhoto(profileModel.getPhoto());
        }
        if ((dto instanceof StudentDTO) && profileModel.getPrimaryGroup() != null) {
            GroupDTO groupDTO = groupConnector.getByName(profileModel.getPrimaryGroup().getName());
            if (groupDTO == null) {
                groupDTO = new GroupDTO();
                groupConvertor.toDTO(profileModel.getPrimaryGroup(), groupDTO);
                groupDTO = groupConnector.save(groupDTO);
                profileModel.getPrimaryGroup().setId(groupDTO.getId());
            }
            ((StudentDTO) dto).setPrimaryGroup(groupDTO);
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO();
            groupReferenceDTO.setGroupId(((StudentDTO) dto).getPrimaryGroup().getId());
            groupReferenceDTO.setStudent((StudentDTO) dto);
            groupReferenceConnector.save(groupReferenceDTO);
        }
        userConnector.save(dto);
    }

    public List<Group> findByStudent() {
        StudentDTO studentDTO = (StudentDTO) getMeAsDto();
        List<Group> groups = new ArrayList<>();
        studentDTO.getGroups().forEach((groupDTO) -> {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            groups.add(group);
        });
        return groups;
    }

    public byte[] getUserPhoto(String userId) {
        UserDTO userDTO = getDTOById(userId);
        return userDTO.getPhoto();
    }

    public byte[] getMyPhoto() {
        if (getMeAsDto().getPhoto().length == 0) {
            throw new UserPhotoNotFoundException("The user does not have a photo!");
        }
        return getMeAsDto().getPhoto();
    }

    public List<Long> findGroupsId() {
        List<GroupDTO> groups = ((StudentDTO) getMeAsDto()).getGroups();
        List<Long> groupsId = new ArrayList<>();
        for (GroupDTO group : groups) {
            groupsId.add(group.getId());
        }
        return groupsId;
    }
}
