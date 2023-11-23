package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.GroupReferencePK;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupGiven extends LessonsIntegrationTest {

    @Given("^groups exist$")
    public void groupsExist(List<Group> groups) {
        List<Long> groupId = new ArrayList<>();
        List<GroupReferenceDTO> groupReferenceDTOS = new ArrayList<>();
        for (Group group : groups) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(group.getDiscipline().getName(), 1L);
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(group.getId(), disciplineDTO);
            groupReferenceDTOS.add(groupReferenceDTO);
        }

        for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().findById(groupReferenceDTO.getDiscipline().getId()).orElse(null);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
                groupReferenceDTO.setDiscipline(disciplineDTO);
            }
            GroupReferenceDTO result = getGroupReferenceConnector().findByGroupId(groupReferenceDTO.getGroupId());
            if (result == null) {
                result = getGroupReferenceConnector().save(groupReferenceDTO);
            }
            groupId.add(result.getGroupId());
        }
        getTestExecutionContext().getDetails().setGroupId(groupId);
    }

    @Given("^teacher has \"(.*)\" discipline with id (.*) contains groups with id \"([^\\\"]*)\"$")
    public void groupsExist(String disciplineName, String disciplineId, String groupIdsString) {

        String[] numberStrings = groupIdsString.split(", ");

        long[] groupIds = new long[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            groupIds[i] = Long.parseLong(numberStrings[i]);
        }

        if (getTestExecutionContext().getIdMapping().get(disciplineId) != null) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getById((Long) getTestExecutionContext().getIdMapping().get(disciplineId));
            getDisciplineConnector().delete(disciplineDTO);
        }

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(disciplineName);
        disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
        disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
        disciplineDTO = getDisciplineConnector().save(disciplineDTO);

        List<GroupReferenceDTO> groupReferenceDTOS = new ArrayList<>();

        for (Long groupId : groupIds) {
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO);
            groupReferenceDTOS.add(groupReferenceDTO);
            getGroupReferenceConnector().save(groupReferenceDTO);
        }

        getTestExecutionContext().registerId(disciplineId, disciplineDTO.getId());
    }

    @Given("^groups with specific id exist$")
    public void groupsExist(DataTable groups) {
        List<Map<String, String>> groupsMap = groups.asMaps();
        for (Map<String, String> groupMap : groupsMap) {
            String groupKey = groupMap.get("discipline");
            String groupStringDiscipline = (String) getTestExecutionContext().replaceId(groupKey);
            if (groupKey != null && !groupKey.equals(groupStringDiscipline)) {
                Long disciplineId = Long.parseLong(groupKey);
                Long groupId = Long.valueOf(groupMap.get("group"));
                DisciplineDTO disciplineDTO = getDisciplineConnector().findById(disciplineId).orElse(null);
                if (disciplineDTO != null) {
                    GroupReferencePK groupReferencePK = new GroupReferencePK(groupId,disciplineId);
                    if(!getGroupReferenceConnector().existsById(groupReferencePK)){
                        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO();
                        groupReferenceDTO.setDiscipline(disciplineDTO);
                        groupReferenceDTO.setGroupId(groupId);
                        getGroupReferenceConnector().save(groupReferenceDTO);
                    }
                }
            }
        }
    }
}
