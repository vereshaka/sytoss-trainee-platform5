package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Given;

public class DisciplineGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" discipline exists$")
    public void disciplineExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(disciplineName);
        discipline = getDisciplineConnector().saveAndFlush(discipline);
        IntegrationTest.getTestContext().setDisciplineId(discipline.getId());
    }
}