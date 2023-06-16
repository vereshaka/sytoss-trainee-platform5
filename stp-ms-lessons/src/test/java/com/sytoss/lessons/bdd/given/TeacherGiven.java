package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;

import io.cucumber.java.en.Given;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" exists$")
    public void teacherExists(String firstName, String lastName) {
        TestExecutionContext.getTestContext().setTeacherId(1L);
    }
}
