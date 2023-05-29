package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisciplineConvertor {

    private final TeacherConvertor teacherConverter;

    public void fromDTO(DisciplineDTO source, Discipline destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        Teacher teacher = new Teacher();
        teacherConverter.fromDTO(source.getTeacher(), teacher);
        destination.setTeacher(teacher);
    }

    public void toDTO(Discipline source, DisciplineDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherConverter.toDTO(source.getTeacher(), teacherDTO);
        destination.setTeacher(teacherDTO);
    }
}
