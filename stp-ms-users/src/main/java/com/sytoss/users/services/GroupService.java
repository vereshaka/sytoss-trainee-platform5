package com.sytoss.users.services;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.GroupNotFoundException;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.services.exceptions.UserTypeNotIdentifiedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService {

    private final GroupConnector groupConnector;

    private final GroupConvertor groupConvertor;

    private final UserService userService;

    private final UserConverter userConverter;

    public Group getById(Long id) {
        GroupDTO groupDTO = getDtoById(id);
        Group group = new Group();
        groupConvertor.fromDTO(groupDTO, group);
        return group;
    }

    private GroupDTO getDtoById(Long id) {
        try {
            GroupDTO groupDTO = groupConnector.findById(id).orElseThrow();
            return groupDTO;
        } catch (EntityNotFoundException e) {
            throw new GroupNotFoundException(id);
        }
    }

    public Group create(Group group) {
        GroupDTO groupDTO = groupConnector.getByName(group.getName());
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupConvertor.toDTO(group, groupDTO);
            groupDTO = groupConnector.save(groupDTO);
            groupConvertor.fromDTO(groupDTO, group);
            return group;
        }
        throw new GroupExistException(group.getName());
    }

    public void assignStudentToGroup(Long groupId, Long studentId) {
        AbstractUser user = userService.getById(studentId);
        if (!(user instanceof Student)) {
            throw new UserTypeNotIdentifiedException("User is not student");
        }
        GroupDTO groupDTO = getDtoById(groupId);
        StudentDTO studentDTO = new StudentDTO();
        userConverter.toDTO(user, studentDTO);
        groupDTO.getStudents().add(studentDTO);
        groupConnector.save(groupDTO);

    }
}