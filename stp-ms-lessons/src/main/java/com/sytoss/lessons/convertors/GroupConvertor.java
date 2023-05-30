package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Group;
import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.stereotype.Component;

@Component
public class GroupConvertor {

    public void toDTO(Group source, GroupDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}
