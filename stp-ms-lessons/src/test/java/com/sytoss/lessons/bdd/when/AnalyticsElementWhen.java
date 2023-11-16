package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AnalyticsElementWhen extends AbstractGiven {

    @When("teacher updates analytics element")
    public void teacherUpdatesAnalyticsElement() {
        String url = "/api/analytics/update";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(getTestExecutionContext().getDetails().getAnalyticsElement(), httpHeaders);
        ResponseEntity<AnalyticsElement> responseEntity = doPost(url, httpEntity, AnalyticsElement.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher makes a migration for discipline (.*)$")
    public void teacherMakesAMigration(String disciplineStringId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        String url = "/api/analytics/migrate/" + disciplineId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<AnalyticsElement>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
