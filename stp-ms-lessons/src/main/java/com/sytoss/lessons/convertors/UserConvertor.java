package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Component
public class UserConvertor {

    public void toTeacher(LinkedHashMap<String, Object> source, Teacher teacher) {
        teacher.setId(Long.valueOf((Integer) source.get("id")));
        teacher.setFirstName((String) source.get("firstName"));
        teacher.setLastName((String) source.get("lastName"));
        teacher.setEmail((String) source.get("email"));
    }

    public void toStudent(LinkedHashMap<String, Object> source, Student student) {
        student.setId(Long.valueOf((Integer) source.get("id")));
        student.setFirstName((String) source.get("firstName"));
        student.setLastName((String) source.get("lastName"));
        student.setEmail((String) source.get("email"));
        Group group = new Group();
        toGroup((LinkedHashMap<String, Object>) source.get("primaryGroup"), group);
        source.get("primaryGroup");
    }

    public void toGroup(LinkedHashMap<String, Object> source, Group destination) {
        Discipline discipline = new Discipline();
        if (source.get("discipline") != null) {
            toDiscipline((LinkedHashMap<String, Object>) source.get("discipline"), discipline);
        }
        destination.setDiscipline(discipline);
        destination.setId(Long.valueOf((Integer) source.get("id")));
        destination.setName((String) source.get("name"));
    }

    public void toDiscipline(LinkedHashMap<String, Object> source, Discipline destination) {
        destination.setId((Long) source.get("id"));
    }
}
