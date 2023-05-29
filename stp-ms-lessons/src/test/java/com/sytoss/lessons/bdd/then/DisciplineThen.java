package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DisciplineThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" discipline$")
    public void disciplineShouldBeReceived(String disciplineName) {
        Discipline discipline = (Discipline) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplineName, discipline.getName());
    }
}
