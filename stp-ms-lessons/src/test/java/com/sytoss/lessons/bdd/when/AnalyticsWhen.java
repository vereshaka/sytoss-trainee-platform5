package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import com.sytoss.lessons.controllers.views.DisciplineGroupSummary;
import com.sytoss.lessons.controllers.views.DisciplineSummary;
import com.sytoss.lessons.controllers.views.StudentDisciplineStatistic;
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
        getTestExecutionContext().getDetails().setDisciplineId(disciplineId);
        String url = "/api/analytics/migrate/" + disciplineId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^user gets ratings by discipline (.*), by exam (.*) and by group (.*) and grade type (.*)$")
    public void teacherGetsRatingsByDisciplineExamGroupGrade(String disciplineStringId, String examStringId, String groupStringId, String gradeType) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(disciplineStringId).toString());
        Long examId = null;
        Long groupId = null;
        if (!Objects.equals(examStringId, "null")) {
            examId = Long.parseLong(getTestExecutionContext().replaceId(examStringId).toString());
        }
        if (!Objects.equals(groupStringId, "null")) {
            groupId = Long.parseLong(getTestExecutionContext().replaceId(groupStringId).toString());
        }
        String url = "/api/analytics/rating/discipline/" + disciplineId + "/group/" + groupId + "/exam/" + examId + "/grade/" + gradeType;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Rating>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher requests analytics for discipline (.*) and student (.*)$")
    public void teacherRequestsAnalyticsForDisciplineAndStudent(String scenarioDisciplineId, String studentId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(scenarioDisciplineId).toString());
        String url = "/api/analytics/discipline/" + disciplineId + "/student/" + studentId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<StudentDisciplineStatistic> responseEntity = doGet(url, httpEntity, StudentDisciplineStatistic.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher requests discipline summary for discipline (.*)$")
    public void teacherRequestsDisciplineSummaryForDiscipline(String scenarioDisciplineId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(scenarioDisciplineId).toString());
        String url = "/api/analytics/discipline/" + disciplineId + "/summary";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<DisciplineGroupSummary> responseEntity = doGet(url, httpEntity, DisciplineGroupSummary.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher requests discipline summary for group with id (.*) and discipline with id (.*)$")
    public void teacherRequestsDisciplineSummaryForDisciplineAndGroup(String groupId, String scenarioDisciplineId) {
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(scenarioDisciplineId).toString());
        String url = "/api/analytics/discipline/" + disciplineId + "/summary/group/" + groupId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<DisciplineSummary> responseEntity = doGet(url, httpEntity, DisciplineSummary.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
