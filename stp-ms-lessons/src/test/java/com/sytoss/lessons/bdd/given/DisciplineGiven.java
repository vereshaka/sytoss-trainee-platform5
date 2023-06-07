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
                disciplineResult = getDisciplineConnector().save(discipline);
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineResult.getId());
        }
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

    @Given("^this teacher has \"(.*)\" discipline$")
    public void teacherHasDiscipline(String disciplineName) {

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }
}
