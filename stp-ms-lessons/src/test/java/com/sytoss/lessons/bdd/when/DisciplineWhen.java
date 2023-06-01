package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        ResponseEntity<Discipline> responseEntity = doPost(url, discipline, new ParameterizedTypeReference<Discipline>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        ResponseEntity<String> responseEntity = doPost(url, discipline, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId();
        ResponseEntity<Discipline> responseEntity = doGet(url, Void.class, Discipline.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}