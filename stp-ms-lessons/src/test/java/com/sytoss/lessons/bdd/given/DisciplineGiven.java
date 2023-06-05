package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class DisciplineGiven extends CucumberIntegrationTest {

    @Given("disciplines exist")
    public void disciplinesExist(List<DisciplineDTO> disciplines) {
        for (DisciplineDTO discipline : disciplines) {
            Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
            TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);
            if (teacherDTO == null) {
                teacherDTO = getTeacherConnector().save(discipline.getTeacher());
            }
            DisciplineDTO disciplineResult = getDisciplineConnector().getByNameAndTeacherId(discipline.getName(), teacherDTO.getId());
            discipline.setTeacher(teacherDTO);
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

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            TeacherDTO teacherDTO = getTeacherConnector().getReferenceById(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacher(teacherDTO);
            getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^this teacher has \"(.*)\" discipline$")
    public void teacherHasDiscipline(String disciplineName) {

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            TeacherDTO teacherDTO = getTeacherConnector().getReferenceById(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacher(teacherDTO);
            disciplineDTO = getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }
}
