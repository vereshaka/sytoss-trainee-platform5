package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentConverter extends AbstractUserConverter {

    private final GroupConvertor groupConvertor;

    public void toDTO(Student source, StudentDTO destination) {
        super.toDTO(source, destination);
        if (source.getGroup() != null) {
            GroupDTO groupDTO = new GroupDTO();
            groupConvertor.toDTO(source.getGroup(), groupDTO);
            destination.setGroup(groupDTO);
        }
    }

    public void fromDTO(StudentDTO source, Student destination) {
        super.fromDTO(source, destination);
        if (source.getGroup() != null) {
            Group group = new Group();
            groupConvertor.fromDTO(source.getGroup(), group);
            destination.setGroup(group);
        }
    }

}
