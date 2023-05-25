package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class GroupGiven extends CucumberIntegrationTest {

    @DataTableType
    public GroupDTO mapGroups(Map<String, String> entry) {
        GroupDTO group = new GroupDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(entry.get("discipline"));
        group.setName(entry.get("group"));
        group.setDiscipline(discipline);
        return group;
    }

    @Given("^groups exist$")
    public void groupsExist(List<GroupDTO> groups) {
        for (GroupDTO groupDTO : groups) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(groupDTO.getDiscipline().getName());
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(groupDTO.getDiscipline().getName());
                disciplineDTO = getDisciplineConnector().save(groupDTO.getDiscipline());
            }
            GroupDTO result = getGroupConnector().getByNameAndDisciplineId(groupDTO.getName(), disciplineDTO.getId());
            groupDTO.setDiscipline(disciplineDTO);
            if (result == null) {
                getGroupConnector().save(groupDTO);
            }
        }
    }
}


