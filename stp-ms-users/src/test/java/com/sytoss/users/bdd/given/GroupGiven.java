package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import io.cucumber.java.en.Given;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" group exist$")
    public void groupExistForDiscipline(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            getGroupConnector().save(groupDTO);
        }
    }
}