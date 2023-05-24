package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
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
        group.setName(entry.get("group name"));
        group.setDiscipline(discipline);
        return group;
    }

    @Given("^groups exist$")
    public void groupsExist(List<GroupDTO> groups) {
        getGroupConnector().saveAll(groups);
    }
}
