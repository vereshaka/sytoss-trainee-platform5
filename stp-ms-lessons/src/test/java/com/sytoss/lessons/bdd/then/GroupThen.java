package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.GroupDTO;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GroupThen extends CucumberIntegrationTest {

    @Then("^groups should received$")
    public void groupsShouldBeReceived(List<GroupDTO> groups) throws JsonProcessingException {
        List<Group> result = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), new TypeReference<List<Group>>() {
        });
        assertEquals(groups.size(), result.size());

        for (GroupDTO group : groups) {
            List<GroupDTO> foundItems = groups.stream().filter(item -> Objects.equals(group.getName(), item.getName()) &&
                    Objects.equals(group.getDiscipline().getName(), item.getDiscipline().getName())).toList();
            if (foundItems.size() == 0) {
                fail("Group with name " + group.getName() + "and discipline with name" + group.getDiscipline().getName() +"not found");
            }
            result.remove(foundItems.get(0));
        }
        assertEquals(0, result.size());
    }
}
