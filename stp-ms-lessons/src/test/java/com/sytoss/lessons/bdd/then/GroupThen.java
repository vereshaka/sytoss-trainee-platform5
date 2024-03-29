package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.GroupAssignment;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupThen extends LessonsIntegrationTest {

    @Then("^groups should be received$")
    public void groupReferencesShouldBeReceived(List<Group> groups) {
        List<Group> results = (List<Group>) getTestExecutionContext().getResponse().getBody();
        assertNotNull(results);
        assertEquals(groups.size(), results.size());

        int quantityOfGroups = 0;

        for (Group result : results) {
            for (Group group : groups)
                if (result.getId().equals(group.getId())) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());
    }

    @Then("^groups of discipline with id (.*) should be received$")
    public void groupsShouldBeReceived(String disciplineId, List<Group> groups) {
        List<GroupAssignment> results = (List<GroupAssignment>) getTestExecutionContext().getResponse().getBody();
        assertNotNull(results);
        assertEquals(groups.size(), results.size());

        int quantityOfGroups = 0;

        for (GroupAssignment result : results) {
            for (Group group : groups)
                if (result.getGroup().getId().equals(group.getId())) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());

        if (getTestExecutionContext().getIdMapping().get(disciplineId) != null) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getById((Long) getTestExecutionContext().getIdMapping().get(disciplineId));
            getDisciplineConnector().delete(disciplineDTO);
        }
    }
}
