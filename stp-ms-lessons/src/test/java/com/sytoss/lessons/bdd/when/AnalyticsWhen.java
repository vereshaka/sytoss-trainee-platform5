package com.sytoss.lessons.bdd.when;
import com.sytoss.domain.bom.analytics.Analytics;

import com.sytoss.lessons.bdd.given.AbstractGiven;
import com.sytoss.lessons.dto.AnalyticsDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class AnalyticsWhen extends AbstractGiven {

    @When("teacher updates analytics element")
    public void teacherUpdatesAnalyticsElement() {
        Analytics analytics = getTestExecutionContext().getDetails().getAnalytics();
        String url = "/api/analytics";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(analytics, httpHeaders);
        ResponseEntity<Analytics> responseEntity = doPost(url, httpEntity, Analytics.class);
        getTestExecutionContext().setResponse(responseEntity);
        AnalyticsDTO analyticsDTO = getAnalyticsConnector().getByDisciplineIdAndExamIdAndStudentId(analytics.getDiscipline().getId(), analytics.getExam().getId(), analytics.getStudent().getId());
        Analytics analytics1 = new Analytics();
        getAnalyticsConvertor().fromDTO(analyticsDTO, analytics1);
        getTestExecutionContext().getDetails().setAnalytics(analytics1);
    }
    @When("^teacher makes a migration for discipline (.*)$")
    public void teacherMakesAMigration(String disciplineStringId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        String url = "/api/analytics/migrate/" + disciplineId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Analytics>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}