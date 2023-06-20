package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;

import io.cucumber.java.en.Given;

import java.util.ArrayList;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" with \"(.*)\" email exists$")
    public void teacherExists(String firstName, String lastName, String email) {
        TestExecutionContext.getTestContext().setTeacherId(1L);
        TestExecutionContext.getTestContext().setToken(generateJWT(new ArrayList<>(), firstName, lastName, email, "teacher"));
    }
}
