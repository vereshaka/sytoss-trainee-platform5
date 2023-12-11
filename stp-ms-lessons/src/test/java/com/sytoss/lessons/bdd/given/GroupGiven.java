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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

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
            String disciplineKey = groupMap.get("discipline");
            String groupStringDiscipline = getTestExecutionContext().replaceId(disciplineKey).toString();
            String groupKey = groupMap.get("group");
            if (disciplineKey != null && !disciplineKey.equals(groupStringDiscipline)) {
                Long disciplineId = Long.parseLong(groupStringDiscipline);
                Long groupId;
                if (groupMap.get("group").contains("*")) {
                    groupKey = getTestExecutionContext().replaceId(groupMap.get("group")).toString();
                    if (!groupKey.contains("*")) {
                        groupId = Long.valueOf(groupKey);
                    } else {
                        groupId = Long.valueOf(groupKey.replace("*g", ""));
                    }
                } else {
                    groupId = Long.valueOf(groupMap.get("group"));
                }

                DisciplineDTO disciplineDTO = getDisciplineConnector().findById(disciplineId).orElse(null);
                if (disciplineDTO != null) {
                    GroupReferencePK groupReferencePK = new GroupReferencePK(groupId, disciplineId);
                    if (!getGroupReferenceConnector().existsById(groupReferencePK)) {
                        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO();
                        groupReferenceDTO.setDiscipline(disciplineDTO);
                        groupReferenceDTO.setGroupId(groupId);
                        getGroupReferenceConnector().save(groupReferenceDTO);
                        getTestExecutionContext().registerId(groupKey, groupId);
                    }
                }
            }
        }
    }

    @Given("student has such groups")
    public void studentHasSuchGroups(DataTable dataTable) {
        List<Map<String, String>> studentsToGroups = dataTable.asMaps();

        Map<Long, List<Group>> studentListMap = new HashMap<>();

        for (Map<String, String> studentToGroup : studentsToGroups) {
            Long studentId = Long.parseLong(studentToGroup.get("studentId"));
            Long groupId = Long.parseLong(studentToGroup.get("groupId"));

            Group group = new Group();
            group.setId(groupId);
            if (studentListMap.get(studentId) != null) {
                List<Group> groups = new ArrayList<>(studentListMap.get(studentId));
                groups.add(group);
                studentListMap.put(studentId, groups);
            } else {
                studentListMap.put(studentId, List.of(group));
            }
        }

        for (Map.Entry<Long, List<Group>> student : studentListMap.entrySet()) {
            when(getUserConnector().getGroupsOfStudent(student.getKey())).thenReturn(student.getValue());
        }
    }
}
