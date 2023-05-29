package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("teacher creates {string} discipline")
    public void topicCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline/create";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpEntity<Discipline> request = new HttpEntity<>(discipline, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, request, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
