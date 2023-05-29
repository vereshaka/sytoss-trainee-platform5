package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class DisciplineGiven extends CucumberIntegrationTest {

    @DataTableType
    public DisciplineDTO mapDiscipline(Map<String, String> entry) {
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(entry.get("discipline"));
        return discipline;
    }

    @Given("disciplines exist")
    public void disciplineExist(List<DisciplineDTO> disciplines) {
        for (DisciplineDTO discipline : disciplines) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(discipline.getName());
            if (disciplineDTO == null) {
                disciplineDTO = getDisciplineConnector().save(discipline);
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
        }
    }

    @Given("^\"(.*)\" discipline exists$")
    public void disciplineExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
    }
}
