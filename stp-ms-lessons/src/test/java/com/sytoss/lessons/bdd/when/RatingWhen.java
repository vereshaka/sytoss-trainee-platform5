package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class RatingWhen extends AbstractGiven {

    @When("teacher updates rating")
    public void teacherUpdatesRating() {
        String url = "/api/rating/update";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(getTestExecutionContext().getDetails().getRating(), httpHeaders);
        ResponseEntity<Rating> responseEntity = doPost(url, httpEntity, Rating.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher makes a migration for discipline (.*)$")
    public void teacherMakesAMigration(String disciplineStringId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        String url = "/api/rating/migrate/" + disciplineId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Rating>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
