package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.GroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService {

    private final GroupConnector groupConnector;

    private final GroupConvertor groupConvertor;

    public List<Group> findByDiscipline(Long disciplineId) {
        List<GroupDTO> groupDTOList = groupConnector.findByDisciplineId(disciplineId);
        List<Group> result = new ArrayList<>();
        for (GroupDTO groupDTO : groupDTOList) {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            result.add(group);
        }
        return result;
    }
}
