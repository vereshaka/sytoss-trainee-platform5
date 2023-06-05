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

    @Given("^teacher with \"(.*)\" firstName and \"(.*)\" middleName and \"(.*)\" lasName doesnt exist$")
    public void teacherNotExist(String firstName, String middleName, String lastname) {
        TeacherDTO teacherDTO = getTeacherConnector().getByLastNameAndMiddleNameAndFirstName(firstName, middleName,lastname);
        if (teacherDTO != null) {
            getTeacherConnector().delete(teacherDTO);
        }
    }

    @Given("teacher with {string} firstName and {string} middleName and {string} lastName exists")
    public void teacherWithFirstNameAndMiddleNameAndLastNameDoesntExist(String firstName, String middleName, String lastName) {
        TeacherDTO teacherDTO = getTeacherConnector().getByLastNameAndMiddleNameAndFirstName(firstName, middleName,lastName);
        if (teacherDTO == null) {
            teacherDTO = new TeacherDTO();
            teacherDTO.setFirstName(firstName);
            teacherDTO.setMiddleName(middleName);
            teacherDTO.setLastName(lastName);
            getTeacherConnector().save(teacherDTO);
        }
        TestExecutionContext.getTestContext().setTeacherId(teacherDTO.getId());
    }
}
