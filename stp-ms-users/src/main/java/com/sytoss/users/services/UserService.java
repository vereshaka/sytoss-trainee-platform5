package com.sytoss.users.services;

import com.sytoss.common.AbstractStpService;
import com.sytoss.domain.bom.exceptions.business.LoadImageException;
import com.sytoss.domain.bom.exceptions.business.NotAllowedTeacherRegistrationException;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.connectors.ImageProviderConnector;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends AbstractStpService {

    private final UserConnector userConnector;

    private final UserConverter userConverter;

    private final GroupConvertor groupConvertor;

    private final GroupReferenceConnector groupReferenceConnector;

    private final GroupConnector groupConnector;

    private final ImageProviderConnector imageProviderConnector;

    @Value("#{new Boolean('${registration.allow-registration}')}")
    private boolean isAllowed;

    public AbstractUser getByUid(String userId) {
        UserDTO foundUser = getDTOByUid(userId);

        if (Objects.isNull(foundUser.getImageName()) && Objects.nonNull(foundUser.getPhoto())) {
            foundUser = saveUserPhoto(foundUser);
        }

        return instantiateUser(foundUser);
    }


    private UserDTO getDTOByUid(String userId) {
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
        if (Objects.isNull(userDto.getImageName()) && Objects.nonNull(userDto.getPhoto())) {
            userDto = saveUserPhoto(userDto);
        }
        return instantiateUser(userDto);
    }

    private UserDTO saveUserPhoto(UserDTO userDto) {
        try {
            String imageName = imageProviderConnector.saveImage(userDto.getPhoto());
            userDto.setImageName(imageName);
            return userConnector.save(userDto);
        } catch (Exception e) {
            log.warn("Could not save user photo!", e);
            return userDto;
        }
    }

    private void updateUserPhoto(String imageName, MultipartFile photo) {
        try {
            imageProviderConnector.saveImage(imageName, photo);
        } catch (Exception e) {
            log.warn("Could not update user photo!");
        }
    }

    private AbstractUser instantiateUser(UserDTO userDto) {
        AbstractUser result;
        UserDTO source = userDto;
        if (userDto instanceof HibernateProxy) {
            source = (UserDTO) ((HibernateProxy) userDto).getHibernateLazyInitializer().getImplementation();
        }
        if (source instanceof TeacherDTO) {
            Teacher teacher = new Teacher();
            userConverter.fromDTO(source, teacher);
            result = teacher;
        } else if (source instanceof StudentDTO) {
            Student student = new Student();
            userConverter.fromDTO((StudentDTO) source, student);
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
            if (!isAllowed) {
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
        dto.setMiddleName(profileModel.getMiddleName());
        if (profileModel.getFirstName() != null) {
            dto.setFirstName(profileModel.getFirstName());
        }
        if (profileModel.getLastName() != null) {
            dto.setLastName(profileModel.getLastName());
        }
        if (profileModel.getPhoto() != null) {
            if (Objects.nonNull(dto.getImageName())) {
                updateUserPhoto(dto.getImageName(), profileModel.getPhoto());
            }

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
        UserDTO userDTO = getDTOByUid(userId);
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

    public AbstractUser getById(Long userId) {
        UserDTO foundUser = getDTOById(userId);

        if (Objects.isNull(foundUser.getImageName()) && Objects.nonNull(foundUser.getPhoto())) {
            foundUser = saveUserPhoto(foundUser);
        }

        return instantiateUser(foundUser);
    }


    private UserDTO getDTOById(Long userId) {
        try {
            return userConnector.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("User not found", e);
        }
    }

}
