package com.sytoss.users.services;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.dto.GroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService {

    private final GroupConnector groupConnector;

    private final GroupConvertor groupConvertor;

    public Group create(Group group) {
        GroupDTO groupDTO = groupConnector.getByName(group.getName());
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupConvertor.toDTO(group, groupDTO);
            groupDTO = groupConnector.save(groupDTO);
            groupConvertor.fromDTO(groupDTO, group);
            return group;
        }
        throw new GroupExistException(group.getName());
    }
}
