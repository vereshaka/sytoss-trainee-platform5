package com.sytoss.lessons.bdd.when;
import com.sytoss.domain.bom.analytics.AnalyticFull;
import com.sytoss.domain.bom.analytics.Analytics;

import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import com.sytoss.lessons.dto.AnalyticsDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Objects;

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
        String url = "/api/analytics/rating/discipline/" + disciplineId+"/group/"+groupId+"/exam/"+examId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Rating>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
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