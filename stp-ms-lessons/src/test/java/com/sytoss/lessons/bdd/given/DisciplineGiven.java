package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Given;

import java.util.List;

public class DisciplineGiven extends CucumberIntegrationTest {

    @Given("disciplines exist")
    public void disciplinesExist(List<DisciplineDTO> disciplines) {
        for (DisciplineDTO discipline : disciplines) {
            DisciplineDTO disciplineResult = getDisciplineConnector().getByNameAndTeacherId(discipline.getName(), discipline.getTeacherId());
            if (disciplineResult == null) {
                getDisciplineConnector().save(discipline);
            }
        }
    }

    @Given("^\"(.*)\" discipline exists for this teacher$")
    public void disciplineExistsForTeacher(String nameDiscipline) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(nameDiscipline);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(nameDiscipline);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^discipline \"(.*)\" doesn't exist$")
    public void disciplineNotExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO != null) {
            getDisciplineConnector().delete(disciplineDTO);
        }
    }

    @Given("^\"(.*)\" discipline exists$")
    public void disciplineExist(String disciplineName) {

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }
}
