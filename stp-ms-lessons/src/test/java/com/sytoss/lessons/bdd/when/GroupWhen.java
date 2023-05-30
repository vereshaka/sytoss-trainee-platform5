package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GroupWhen extends CucumberIntegrationTest {

    @When("^receive all groups by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId() + "/groups";
        ResponseEntity<List<Group>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Group>>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
