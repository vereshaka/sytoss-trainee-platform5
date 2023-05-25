package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.GroupDTO;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupThen extends CucumberIntegrationTest {

    @Then("^groups should received$")
    public void groupsShouldBeReceived(List<GroupDTO> groups) throws JsonProcessingException {
        List<Group> results = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), new TypeReference<>() {
        });
        assertEquals(groups.size(), results.size());

        int quantityOfGroups = 0;

        for (Group result : results) {
            for (GroupDTO groupDTO : groups)
                if (result.getName().equals(groupDTO.getName())) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());
    }
}
