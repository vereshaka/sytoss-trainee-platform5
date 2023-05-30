package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("teacher creates {string} discipline")
    public void topicCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline/create";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        ResponseEntity<String> responseEntity = doPost(url, discipline, new ParameterizedTypeReference<String>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
