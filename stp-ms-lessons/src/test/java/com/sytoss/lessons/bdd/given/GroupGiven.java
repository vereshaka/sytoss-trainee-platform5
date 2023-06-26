package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^groups exist$")
    public void groupsExist(List<GroupReferenceDTO> groups) {
        for (GroupReferenceDTO groupReferenceDTO : groups) {
            Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(groupReferenceDTO.getDiscipline().getId());
            DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
                getDisciplineConnector().save(disciplineDTO);
            }
            GroupReferenceDTO result = getGroupReferenceConnector().findByGroupId(groupReferenceDTO.getGroupId());
            if (result == null) {
                getGroupReferenceConnector().save(groupReferenceDTO);
            }
        }
    }
}