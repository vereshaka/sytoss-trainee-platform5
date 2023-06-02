package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DisciplineGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" exists$")
    public void teacherExists(String firstName, String lastName) {
        TeacherDTO teacherDTO = getTeacherConnector().getByLastNameAndFirstName(lastName, firstName);
        if (teacherDTO == null) {
            TeacherDTO teacher = new TeacherDTO();
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacherDTO = getTeacherConnector().save(teacher);
        }
        TestExecutionContext.getTestContext().setTeacherId(teacherDTO.getId());
    }

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

        Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
        TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);

        assert teacherDTO != null;

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacher(teacherDTO);
            getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^this teacher has \"(.*)\" discipline$")
    public void teacherHasDiscipline(String disciplineName) {
        Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
        TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);

        assert teacherDTO != null;

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacher(teacherDTO);
            disciplineDTO = getDisciplineConnector().saveAndFlush(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }
}
