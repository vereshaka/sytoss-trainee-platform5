package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DisciplineThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" discipline$")
    public void disciplineShouldBeReceived(String disciplineName) throws JsonProcessingException {
        Discipline discipline = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), Discipline.class);
        assertEquals(disciplineName, discipline.getName());
    }
}
