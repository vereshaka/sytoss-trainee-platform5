package com.sytoss.lessons.bdd.when;
import com.sytoss.domain.bom.analytics.Analytic;

import com.sytoss.domain.bom.analytics.AnalyticFull;
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
        Analytic analytic = getTestExecutionContext().getDetails().getAnalytic();
        String url = "/api/analytics";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(analytic, httpHeaders);
        ResponseEntity<Analytic> responseEntity = doPost(url, httpEntity, Analytic.class);
        getTestExecutionContext().setResponse(responseEntity);
        AnalyticsDTO analyticsDTO = getAnalyticsConnector().getByDisciplineIdAndExamIdAndStudentId(analytic.getDiscipline().getId(), analytic.getExam().getId(), analytic.getStudent().getId());
        Analytic analytic1 = new Analytic();
        getAnalyticsConvertor().fromDTO(analyticsDTO,analytic1);
        getTestExecutionContext().getDetails().setAnalytic(analytic1);
    }
    @When("^teacher makes a migration for discipline (.*)$")
    public void teacherMakesAMigration(String disciplineStringId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        String url = "/api/analytics/migrate/" + disciplineId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Analytic>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher requests analytics for discipline (.*) and student (.*)")
    public void teacherRequestsAnalyticsForDisciplineAndStudent(String scenarionDisciplineId, String studentId){
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(scenarionDisciplineId).toString());
        String url = "/api/analytics/discipline/" + disciplineId + "/student/" + studentId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<AnalyticFull> responseEntity = doGet(url, httpEntity, AnalyticFull.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}