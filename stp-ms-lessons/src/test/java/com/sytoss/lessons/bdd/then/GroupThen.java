package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupThen extends CucumberIntegrationTest {

    @Then("^groups should received$")
    public void groupsShouldBeReceived(List<GroupReferenceDTO> groups) {
        List<Group> results = (List<Group>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(results);
        assertEquals(groups.size(), results.size());

        int quantityOfGroups = 0;

        for (Group result : results) {
            for (GroupReferenceDTO groupDTO : groups)
                if (result.getId().equals(groupDTO.getGroupId())) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());
    }

//    @Then("^\"(.*)\" group should be for \"(.*)\" discipline$")
//    public void groupShouldBe(String groupName, String disciplineName) {
//        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, TestExecutionContext.getTestContext().getDisciplineId());
//        assertNotNull(groupDTO);
//        assertEquals(disciplineName, groupDTO.getDiscipline().getName());
//    }
}
