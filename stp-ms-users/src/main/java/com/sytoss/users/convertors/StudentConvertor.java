package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentConvertor {

    public void toDTO(Student source, StudentDTO destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setMiddleName(source.getMiddleName());
        destination.setEmail(source.getEmail());
    }

    public void fromDTO(StudentDTO source, Student destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setMiddleName(source.getMiddleName());
        destination.setEmail(source.getEmail());
    }
}
