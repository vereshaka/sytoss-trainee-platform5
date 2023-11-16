package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService extends AbstractService {

    private final GroupReferenceConnector groupReferenceConnector;

    private final DisciplineConvertor disciplineConvertor;

    public List<Group> findGroups() {
        List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId_TeacherId(getCurrentUser().getId());
        if (!groupReferenceDTOS.isEmpty()) {
            List<Group> groups = new ArrayList<>();
            for (GroupReferenceDTO referenceDTO : groupReferenceDTOS) {
                Discipline discipline = new Discipline();
                disciplineConvertor.fromDTO(referenceDTO.getDiscipline(), discipline);
                Group group = new Group();
                group.setId(referenceDTO.getGroupId());
                group.setDiscipline(discipline);
                if (groups.stream().filter(item -> item.getId().equals(referenceDTO.getGroupId())).toList().size() == 0) {
                    groups.add(group);
                }

            }
            return groups;
        }
        return null;
    }

    public List<Group> getGroups(Long disciplineId) {
        List<GroupReferenceDTO> groups = groupReferenceConnector.findByDisciplineId(disciplineId);
        List<Group> result = new ArrayList<>();
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        for (GroupReferenceDTO item : groups) {
            Group group = new Group();
            group.setId(item.getGroupId());
            group.setDiscipline(discipline);
            result.add(group);
        }
        return result;
    }
}
