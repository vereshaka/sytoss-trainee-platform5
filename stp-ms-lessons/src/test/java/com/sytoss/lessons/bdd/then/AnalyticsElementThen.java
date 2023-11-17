package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.lessons.analytics.RatingModel;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyticsElementThen extends AbstractGiven {
    @Then("^grade equals (.*)$")
    public void gradeEquals(Double grade) {
        AnalyticsElement analyticsElement = (AnalyticsElement) getTestExecutionContext().getResponse().getBody();
        assertEquals(grade, analyticsElement.getGrade());
    }

    @Then("analytics elements should be")
    public void analyticsShouldBe(DataTable dataTable) throws ParseException {
        List<AnalyticsElement> analyticsElementsFromResponse = (List<AnalyticsElement>) getTestExecutionContext().getResponse().getBody();
        List<Map<String, String>> analyticsMapList = dataTable.asMaps();
        List<AnalyticsElement> analyticsElementList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        for (Map<String, String> analyticsMap : analyticsMapList) {
            AnalyticsElement analyticsElement = new AnalyticsElement();

            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
            Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
            Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
            String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");

            analyticsElement.setExamId(examId);
            analyticsElement.setDisciplineId(disciplineId);
            analyticsElement.setStudentId(studentId);
            analyticsElement.setPersonalExamId(personalExamId);

            if (analyticsMap.get("examAssigneeId") != null) {
                analyticsElement.setExamAssigneeId(Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examAssigneeId").trim()).toString()));
            }
            if (analyticsMap.get("grade") != null) {
                analyticsElement.setGrade(Double.parseDouble(analyticsMap.get("grade").trim()));
            }
            if (analyticsMap.get("timeSpent") != null) {
                analyticsElement.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
            }
            if (analyticsMap.get("startDate") != null) {
                analyticsElement.setStartDate(sdf.parse(analyticsMap.get("startDate").trim()));
            }

            analyticsElementList.add(analyticsElement);
        }

        assertEquals(analyticsElementList.size(), analyticsElementsFromResponse.size());
        for (AnalyticsElement analyticsElementFromFeature : analyticsElementList) {
            for (AnalyticsElement analyticsElementFromResponse : analyticsElementsFromResponse) {
                assertEquals(analyticsElementFromFeature.getDisciplineId(), analyticsElementFromResponse.getDisciplineId());
                assertEquals(analyticsElementFromFeature.getExamId(), analyticsElementFromResponse.getExamId());
                assertEquals(analyticsElementFromFeature.getStudentId(), analyticsElementFromResponse.getStudentId());
                assertEquals(analyticsElementFromFeature.getPersonalExamId(), analyticsElementFromResponse.getPersonalExamId());
                assertEquals(analyticsElementFromFeature.getGrade(), analyticsElementFromResponse.getGrade());
                assertEquals(analyticsElementFromFeature.getStartDate(), analyticsElementFromResponse.getStartDate());
                analyticsElementsFromResponse.remove(analyticsElementFromResponse);
                break;
            }
        }
    }

    @Then("rating models should be")
    public void ratingModelsShouldBe(List<RatingModel> ratingModels) {
        List<RatingModel> ratingsModelsFromResponse = (List<RatingModel>) getTestExecutionContext().getResponse().getBody();
        for (RatingModel ratingModel : ratingModels) {
            for (RatingModel ratingModelFromResponse : ratingsModelsFromResponse) {
                assertEquals(ratingModel.getStudentId(), ratingModelFromResponse.getStudentId());
                assertEquals(ratingModel.getAvgGrade(), ratingModelFromResponse.getAvgGrade());
                assertEquals(ratingModel.getAvgTimeSpent(), ratingModelFromResponse.getAvgTimeSpent());
                ratingsModelsFromResponse.remove(ratingModelFromResponse);
                break;
            }
        }
    }
}
