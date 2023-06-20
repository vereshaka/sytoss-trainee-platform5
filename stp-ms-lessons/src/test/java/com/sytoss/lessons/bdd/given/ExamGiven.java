package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import io.cucumber.java.en.Given;

public class ExamGiven extends CucumberIntegrationTest {

    @Given("\"{word}\" discipline has \"{word}\" group")
    public void disciplineHasGroup(String disciplineName, String groupName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(groupName);
        groupDTO.setDiscipline(disciplineDTO);

        getGroupConnector().save(groupDTO);
    }
}
