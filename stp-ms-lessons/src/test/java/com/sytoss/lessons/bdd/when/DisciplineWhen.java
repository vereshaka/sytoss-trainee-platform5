package com.sytoss.lessons.bdd.when;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDicipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId();
        ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}