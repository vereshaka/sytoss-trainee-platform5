package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.lessons.analytics.RatingModel;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

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

    @When("^teacher gets ratings by discipline (.*), by exam (.*) and by group (.*)$")
    public void teacherGetsRatingsByDisciplineD(String disciplineStringId,String examStringId,String groupStringId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        Long examId = null;
        Long groupId = null;

        if(!Objects.equals(examStringId, "null")){
            examId = Long.parseLong(getTestExecutionContext().replaceId(examStringId).toString());
        }
        if(!Objects.equals(groupStringId, "null")){
            groupId = Long.parseLong(getTestExecutionContext().replaceId(groupStringId).toString());
        }
        String url = "/api/analytics/rating/discipline/" + disciplineId+"/group/"+examId+"/exam/"+groupId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<RatingModel>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
