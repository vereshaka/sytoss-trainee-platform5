package com.sytoss.lessons.bdd.when;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class GroupWhen extends CucumberIntegrationTest {

    @When("^receive all groups by \"SQL\" discipline$")
    public void requestSentFindGroupsByDicipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId() + "/groups";
        ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
