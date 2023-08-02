package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService extends AbstractService {

    private final GroupReferenceConnector groupReferenceConnector;

    public List<Group> findGroups() {
        List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId_TeacherId(getCurrentUser().getId());
        if (!groupReferenceDTOS.isEmpty()) {
            List<Group> groups = new ArrayList<>();
            for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
                Group group = new Group();
                group.setId(groupReferenceDTO.getGroupId());
                groups.add(group);
            }
            return groups;
        }
        return null;
    }
}
