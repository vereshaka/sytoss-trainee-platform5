package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class GroupWhen extends CucumberIntegrationTest {

    @When("^receive all groups by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDicipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId() + "/groups";
        ResponseEntity<List> responseEntity = doGet(url, Void.class, List.class);
        IntegrationTest.getTestContext().setListOfGroupResponse(responseEntity);
    }
}
