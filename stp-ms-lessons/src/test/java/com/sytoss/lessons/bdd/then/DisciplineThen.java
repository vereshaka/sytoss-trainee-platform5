package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DisciplineThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" discipline should be received$")
    public void disciplineShouldBeReceived(String disciplineName) {
        Discipline discipline = (Discipline) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplineName, discipline.getName());
    }

    @Then("^\"(.*)\" discipline should exist$")
    public void disciplineShouldExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        Assertions.assertEquals(disciplineName, disciplineDTO.getName());
    }
}
