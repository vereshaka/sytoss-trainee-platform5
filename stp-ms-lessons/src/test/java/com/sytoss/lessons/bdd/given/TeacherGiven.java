package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.TeacherDTO;

import io.cucumber.java.en.Given;

public class TeacherGiven extends CucumberIntegrationTest {

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
            TeacherDTO teacherDTO1 = new TeacherDTO();
            teacherDTO1.setFirstName(firstName);
            teacherDTO1.setMiddleName(middleName);
            teacherDTO1.setLastName(lastName);
            getTeacherConnector().save(teacherDTO1);
        }
    }
}
