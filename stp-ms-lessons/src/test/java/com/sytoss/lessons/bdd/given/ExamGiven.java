package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Given;

public class ExamGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" discipline has group with id (.*)$")
    public void disciplineHasGroup(String disciplineName, Long groupId) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName,getTestExecutionContext().getDetails().getTeacherId());
        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO);
        getGroupReferenceConnector().save(groupReferenceDTO);
        getTestExecutionContext().getDetails().setGroupReferenceId(groupId);
    }
}
