package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
public class GroupThen extends UsersIntegrationTest {

    @Then("^this student should have \"(.*)\" group$")
    public void studentShouldHasGroup(String groupName) {
        StudentDTO studentDTO = (StudentDTO) getUserConnector().getByUid(getTestExecutionContext().getDetails().getUser().getUid());
        List<GroupDTO> filter = studentDTO.getGroups().stream().filter(item -> item.getName().equals(groupName)).toList();
        assertEquals(1, filter.size());
    }

    @Then("^\"(.*)\" group should exist$")
    public void groupShouldBe(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        assertNotNull(groupDTO);
    }

    @And("^group name should be \"(.*)\"")
    public void groupNameShouldBe(String groupName) {
        Group group = (Group) getTestExecutionContext().getResponse().getBody();
        assertEquals(groupName,group.getName());
    }
}
