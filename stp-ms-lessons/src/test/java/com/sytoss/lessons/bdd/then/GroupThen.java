package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupThen extends CucumberIntegrationTest {

    @Then("^groups of discipline with id (.*) should be received$")
    public void groupsShouldBeReceived(String disciplineId, DataTable dataTable) {
        List<Group> results = (List<Group>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(results);

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<Long> groupIds = new ArrayList<>();

        for (Map<String, String> row : rows) {
            String group = row.get("group");
            groupIds.add(Long.parseLong(group));
        }
        assertEquals(groupIds.size(), results.size());

        int quantityOfGroups = 0;

        for (Group result : results) {
            for (Long groupId : groupIds)
                if (result.getId().equals(groupId)) {
                    quantityOfGroups++;
                }
        }
        assertEquals(quantityOfGroups, results.size());

        if (TestExecutionContext.getTestContext().getIdMapping().get(disciplineId) != null) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getById(TestExecutionContext.getTestContext().getIdMapping().get(disciplineId));
            getDisciplineConnector().delete(disciplineDTO);
        }
    }
}
