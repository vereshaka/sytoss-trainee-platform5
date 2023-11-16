package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingThen extends AbstractGiven {
    @Then("^grade equals (.*)$")
    public void gradeEquals(Double grade) {
        Rating rating = (Rating) getTestExecutionContext().getResponse().getBody();
        assertEquals(grade, rating.getGrade());
    }

    @Then("ratings should be")
    public void ratingsShouldBe(DataTable dataTable) {
        List<Rating> ratingsFromResponse = (List<Rating>) getTestExecutionContext().getResponse().getBody();
        List<Map<String, String>> ratingMapList = dataTable.asMaps();
        List<Rating> ratingList = new ArrayList<>();

        for (Map<String, String> ratingMap : ratingMapList) {
            Rating rating = new Rating();

            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(ratingMap.get("disciplineId").trim()).toString());
            Long examId = Long.parseLong(getTestExecutionContext().replaceId(ratingMap.get("examId").trim()).toString());
            Long studentId = Long.parseLong(getTestExecutionContext().replaceId(ratingMap.get("studentId").trim()).toString());
            String personalExamId = ratingMap.get("personalExamId").trim().replace("*", "");

            rating.setExamId(examId);
            rating.setDisciplineId(disciplineId);
            rating.setStudentId(studentId);
            rating.setPersonalExamId(personalExamId);

            if (ratingMap.get("examAssigneeId") != null) {
                rating.setExamAssigneeId(Long.parseLong(getTestExecutionContext().replaceId(ratingMap.get("examAssigneeId").trim()).toString()));
            }
            if (ratingMap.get("grade") != null) {
                rating.setGrade(Double.parseDouble(ratingMap.get("grade").trim()));
            }
            if (ratingMap.get("timeSpent") != null) {
                rating.setTimeSpent(Long.parseLong(ratingMap.get("timeSpent").trim()));
            }

            ratingList.add(rating);
        }

        assertEquals(ratingList.size(), ratingsFromResponse.size());
        for (Rating ratingFromFeature : ratingList) {
            for (Rating ratingFromResponse : ratingsFromResponse) {
                assertEquals(ratingFromFeature.getDisciplineId(), ratingFromResponse.getDisciplineId());
                assertEquals(ratingFromFeature.getExamId(), ratingFromResponse.getExamId());
                assertEquals(ratingFromFeature.getStudentId(), ratingFromResponse.getStudentId());
                assertEquals(ratingFromFeature.getPersonalExamId(), ratingFromResponse.getPersonalExamId());
                assertEquals(ratingFromFeature.getGrade(), ratingFromResponse.getGrade());
                ratingsFromResponse.remove(ratingFromResponse);
                break;
            }
        }


    }
}
