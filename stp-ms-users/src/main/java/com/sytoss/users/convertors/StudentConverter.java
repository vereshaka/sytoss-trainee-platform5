package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter extends AbstractUserConverter {

    public void toDTO(Student source, StudentDTO destination) {
        super.toDTO(source, destination);
        destination.setPrimaryGroupId(source.getPrimaryGroup().getId());
    }

    public void fromDTO(StudentDTO source, Student destination) {
        super.fromDTO(source, destination);
        destination.setPrimaryGroup(new Group());
        destination.getPrimaryGroup().setId(source.getId());
    }

}
