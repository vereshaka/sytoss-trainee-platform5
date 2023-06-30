package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import io.cucumber.java.en.Given;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" group exists$")
    public void groupExist(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            getGroupConnector().save(groupDTO);
        }
    }

    @Given("^\"(.*)\" group doesnt exist$")
    public void groupNotExist(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        if (groupDTO != null) {
            getUserConnector().deleteAll();
            getGroupConnector().delete(groupDTO);
        }
    }
}