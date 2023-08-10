package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.dto.GroupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GroupConvertor {

    public void fromDTO(GroupDTO source, Group destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setCountOfStudents(source.getStudents().size());
    }

    public void toDTO(Group source, GroupDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}
