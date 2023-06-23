package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class GroupWhen extends CucumberIntegrationTest {

    @When("^receive all groups by discipline with id (.*)$")
    public void requestSentFindGroupsByDiscipline(String disciplineId) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getIdMapping().get(disciplineId) + "/groups";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Group>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
