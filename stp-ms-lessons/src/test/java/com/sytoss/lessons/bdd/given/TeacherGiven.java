package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;

import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" exists$")
    public void teacherExists(String firstName, String lastName) {
        TestExecutionContext.getTestContext().setTeacherId(1L);
    }

    @Given("^teachers exist$")
    public void teachersExists(List<Teacher> teachers) {
        Long id = 1L;
        List<Teacher> teacherList = new ArrayList<>();
       /* for(String teacher : teachers){
            Teacher teacher1 = new Teacher();
            teacher1.setId(id);
            id++;
            teacherList.add(teacher1);
        }
        TestExecutionContext.getTestContext().setTeacherList(teacherList);*/
    }
}
