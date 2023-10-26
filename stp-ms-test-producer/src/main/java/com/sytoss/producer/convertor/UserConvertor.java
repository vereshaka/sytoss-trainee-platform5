package com.sytoss.producer.convertor;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class UserConvertor {

    public void toTeacher(Map<String, Object> source, Teacher teacher) {
        teacher.setId(Long.valueOf(source.get("id").toString()));
        teacher.setFirstName((String) source.get("firstName"));
        teacher.setLastName((String) source.get("lastName"));
        teacher.setEmail((String) source.get("email"));
    }

    public void toStudent(Map<String, Object> source, Student student) {
        student.setId(Long.valueOf(source.get("id").toString()));
        student.setFirstName((String) source.get("firstName"));
        student.setLastName((String) source.get("lastName"));
        student.setEmail((String) source.get("email"));
        Group group = new Group();
        toGroup((Map<String, Object>) source.get("primaryGroup"), group);
        student.setPrimaryGroup(group);
    }

    public void toGroup(Map<String, Object> source, Group destination) {
        Discipline discipline = new Discipline();
        if (source.get("discipline") != null) {
            toDiscipline((Map<String, Object>) source.get("discipline"), discipline);
            destination.setDisciplines(List.of(discipline));
        }
        destination.setId(Long.valueOf((Integer) source.get("id")));
        destination.setName((String) source.get("name"));
    }

    public void toDiscipline(Map<String, Object> source, Discipline destination) {
        destination.setId((Long) source.get("id"));
    }
}
