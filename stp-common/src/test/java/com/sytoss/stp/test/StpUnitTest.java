package com.sytoss.stp.test;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith({MockitoExtension.class})
public abstract class StpUnitTest {

    protected Group createGroup(String groupName, Discipline discipline) {
        Group group = new Group();
        group.setName(groupName);
        group.setDiscipline(discipline);
        return group;
    }

    protected Discipline createDiscipline(String disciplineName, Teacher teacher) {
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        discipline.setTeacher(teacher);
        return discipline;
    }

    protected Teacher createTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        return teacher;
    }
}
