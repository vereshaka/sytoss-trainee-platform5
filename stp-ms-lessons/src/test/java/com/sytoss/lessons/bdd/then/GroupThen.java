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
        List<GroupReferenceDTO> results = (List<GroupReferenceDTO>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(results);
        assertEquals(groups.size(), results.size());

        int quantityOfGroups = 0;

        for (GroupReferenceDTO result : results) {
            for (GroupReferenceDTO groupDTO : groups)
                if (result.getGroupId().equals(groupDTO.getGroupId())) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());
    }
}
