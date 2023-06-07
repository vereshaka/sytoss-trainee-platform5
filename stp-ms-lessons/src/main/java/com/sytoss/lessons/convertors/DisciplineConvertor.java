package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.DisciplineDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisciplineConvertor {

    public void fromDTO(DisciplineDTO source, Discipline destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        Teacher teacher = new Teacher();
        teacher.setId(source.getTeacherId());
        destination.setTeacher(teacher);
    }

    public void toDTO(Discipline source, DisciplineDTO destination) {
        if (source != null) {
            destination.setId(source.getId());
            destination.setName(source.getName());
            destination.setTeacherId(source.getTeacher().getId());
        }
    }
}
