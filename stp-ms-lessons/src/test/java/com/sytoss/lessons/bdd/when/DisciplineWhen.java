package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("teacher creates {string} discipline")
    public void disciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline/create";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        ResponseEntity<Discipline> responseEntity = doPost(url, discipline, new ParameterizedTypeReference<Discipline>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("teacher creates existing {string} discipline")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline/create";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        ResponseEntity<String> responseEntity = doPost(url, discipline, new ParameterizedTypeReference<String>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
