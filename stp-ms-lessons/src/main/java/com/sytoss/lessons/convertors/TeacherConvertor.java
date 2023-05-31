package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.TeacherDTO;
import org.springframework.stereotype.Component;

@Component
public class TeacherConvertor {

    public void fromDTO(TeacherDTO source, Teacher destination) {
        if (source != null) {
            destination.setId(source.getId());
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
        }
    }

    public void toDTO(Teacher source, TeacherDTO destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}
