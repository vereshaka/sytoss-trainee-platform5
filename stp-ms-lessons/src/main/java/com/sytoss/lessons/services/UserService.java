package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.UserConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService extends AbstractService {

    private final UserConnector userConnector;

    private final GroupService groupService;

    public List<Student> getStudents(Long disciplineId) {
        List<Student> students = new ArrayList<>();
        List<Group> groups = groupService.getGroups(disciplineId);
        for (Group group : groups) {
            List<Student> studentsByGroup = userConnector.getStudentOfGroup(group.getId());
            studentsByGroup.forEach(student -> {
                if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                    students.add(student);
                }
            });
        }
        return students;
    }

    public List<Student> getStudentsOfGroup(Long groupId) {
        List<Student> students = new ArrayList<>();
        List<Student> studentsByGroup = userConnector.getStudentOfGroup(groupId);
        studentsByGroup.forEach(student -> {
            if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                students.add(student);
            }
        });
        return students;
    }
}
