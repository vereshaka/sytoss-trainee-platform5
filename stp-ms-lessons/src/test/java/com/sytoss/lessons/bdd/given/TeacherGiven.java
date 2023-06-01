package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TeacherDTO;
import io.cucumber.java.en.Given;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" exists$")
    public void teacherExists(String firstName, String lastName) {
        TeacherDTO teacherDTO = getTeacherConnector().getByFirstNameAndLastName(firstName, lastName);
        if (teacherDTO == null) {
            TeacherDTO teacher = new TeacherDTO();
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacherDTO = getTeacherConnector().save(teacher);
        }
        TestExecutionContext.getTestContext().setTeacherId(teacherDTO.getId());
    }
}