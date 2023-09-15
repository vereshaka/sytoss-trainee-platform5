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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
            GroupDTO groupDTO = groupConnector.findById(id).orElseThrow(EntityNotFoundException::new);
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

    public void assignStudentToGroup(Long groupId, String studentId) {
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

    public List<Student> getStudents(Long groupId) {
        GroupDTO groupDTO = getDtoById(groupId);
        List<Student> students = new ArrayList<>();
        for (StudentDTO studentDTO : groupDTO.getStudents()) {
            Student student = new Student();
            userConverter.fromDTO(studentDTO, student);
            students.add(student);
        }
        Locale ukrainianLocale = Locale.forLanguageTag("uk-UA");
        Collator collator = Collator.getInstance(ukrainianLocale);
        collator.setStrength(Collator.PRIMARY);
        students.sort((prevStudent, nextStudent) -> collator.compare(prevStudent.getLastName(), nextStudent.getLastName()));
        return students;
    }

    public List<Group> getAllGroups() {
        List<GroupDTO> groupDTOS = groupConnector.findAll();
        List<Group> groups = new ArrayList<>();
        for (GroupDTO groupDTO : groupDTOS) {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            groups.add(group);
        }
        return groups;
    }
}
