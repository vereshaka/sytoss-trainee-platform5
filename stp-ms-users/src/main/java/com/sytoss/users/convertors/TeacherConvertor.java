package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TeacherConvertor {

    public void fromDTO(TeacherDTO source, Teacher destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }

    public void toDTO(Teacher source, TeacherDTO destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}
