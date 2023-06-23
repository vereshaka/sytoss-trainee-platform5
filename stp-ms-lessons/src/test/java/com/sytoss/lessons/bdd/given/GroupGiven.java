package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^this teacher has \"(.*)\" discipline with id (.*) contains groups with id \"([^\\\"]*)\"$")
    public void groupsExist(String disciplineName, String disciplineId, String groupIdsString) {

        String[] numberStrings = groupIdsString.split(", ");

        long[] groupIds = new long[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            groupIds[i] = Long.parseLong(numberStrings[i]);
        }

        if (TestExecutionContext.getTestContext().getIdMapping().get(disciplineId) != null) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getById(TestExecutionContext.getTestContext().getIdMapping().get(disciplineId));
            getDisciplineConnector().delete(disciplineDTO);
        }

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(disciplineName);
        disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
        disciplineDTO = getDisciplineConnector().save(disciplineDTO);

        List<GroupReferenceDTO> groupReferenceDTOS = new ArrayList<>();

        for (Long groupId : groupIds) {
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO.getId());
            groupReferenceDTOS.add(groupReferenceDTO);
            getGroupReferenceConnector().save(groupReferenceDTO);
        }

        TestExecutionContext.getTestContext().registerId(disciplineId, disciplineDTO.getId());
    }
}