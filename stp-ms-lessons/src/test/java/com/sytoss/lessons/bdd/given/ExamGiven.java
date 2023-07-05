package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Given;

public class ExamGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" discipline has group with id (.*)$")
    public void disciplineHasGroup(String disciplineName, Long groupId) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName,TestExecutionContext.getTestContext().getTeacherId());
        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO);
        getGroupReferenceConnector().save(groupReferenceDTO);
        TestExecutionContext.getTestContext().setGroupReferenceId(groupId);
    }
}
