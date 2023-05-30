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

    @Given("teacher {word} {word} exists")
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
    public void thisExamHasAnswers(List<DisciplineDTO> disciplines) {
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

    @Given("{string} discipline doesn't exist")
    public void topicExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO != null) {
            getDisciplineConnector().delete(disciplineDTO);
        }
    }
}
