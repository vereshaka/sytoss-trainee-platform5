package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyticsThen extends AbstractGiven {
    @Then("^updated analytic element should be$")
    public void gradeEquals(DataTable dataTable) {
        Analytics analyticsFromResponse = getTestExecutionContext().getDetails().getAnalytics();

        Map<String,String> analyticsMap = dataTable.asMaps().get(0);
        Analytics analytics = new Analytics();
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
        Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
        Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
        String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");
        Exam exam = new Exam();
        exam.setId(examId);
        analytics.setExam(exam);
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        analytics.setDiscipline(discipline);
        Student student = new Student();
        student.setId(studentId);
        analytics.setStudent(student);
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(personalExamId);

        AnalyticGrade grade = new AnalyticGrade();
        if (analyticsMap.get("grade") != null) {
            grade.setGrade(Double.parseDouble(analyticsMap.get("grade").trim()));
        }
        if (analyticsMap.get("timeSpent") != null) {
            grade.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
        }
        analytics.setGrade(grade);

        assertEquals(analyticsFromResponse.getGrade().getGrade(), analytics.getGrade().getGrade());
    }

    @Then("analytics elements should be")
    public void analyticsShouldBe(DataTable dataTable) throws ParseException {
        List<Analytics> analyticsElementsFromResponse = (List<Analytics>) getTestExecutionContext().getResponse().getBody();
        List<Map<String, String>> analyticsMapList = dataTable.asMaps();
        List<Analytics> analyticsElementList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        for (Map<String, String> analyticsMap : analyticsMapList) {
            Analytics analytics = new Analytics();
            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
            Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
            Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
            String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");
            Exam exam = new Exam();
            exam.setId(examId);
            analytics.setExam(exam);
            Discipline discipline = new Discipline();
            discipline.setId(disciplineId);
            analytics.setDiscipline(discipline);
            Student student = new Student();
            student.setId(studentId);
            analytics.setStudent(student);
            PersonalExam personalExam = new PersonalExam();
            personalExam.setId(personalExamId);
            analytics.setPersonalExam(personalExam);

            if (analyticsMap.get("examAssigneeId") != null) {
                ExamAssignee examAssignee = new ExamAssignee();
                examAssignee.setId(Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examAssigneeId").trim()).toString()));
                analytics.getExam().getExamAssignees().add(examAssignee);
            }
            AnalyticGrade grade = new AnalyticGrade();
            if (analyticsMap.get("grade") != null) {
                grade.setGrade(Double.parseDouble(analyticsMap.get("grade").trim()));
            }
            if (analyticsMap.get("timeSpent") != null) {
                grade.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
            }
            if (analyticsMap.get("startDate") != null) {
                analytics.setStartDate(sdf.parse(analyticsMap.get("startDate").trim()));
            }
            analytics.setGrade(grade);
            analyticsElementList.add(analytics);
        }
        assertEquals(analyticsElementList.size(), analyticsElementsFromResponse.size());
        for (Analytics analyticsElementFromFeature : analyticsElementList) {
            for (Analytics analyticsElementFromResponse : analyticsElementsFromResponse) {
                assertEquals(analyticsElementFromFeature.getDiscipline().getId(), analyticsElementFromResponse.getDiscipline().getId());
                assertEquals(analyticsElementFromFeature.getExam().getId(), analyticsElementFromResponse.getExam().getId());
                assertEquals(analyticsElementFromFeature.getStudent().getId(), analyticsElementFromResponse.getStudent().getId());
                assertEquals(analyticsElementFromFeature.getPersonalExam().getId(), analyticsElementFromResponse.getPersonalExam().getId());
                assertEquals(analyticsElementFromFeature.getGrade().getGrade(), analyticsElementFromResponse.getGrade().getGrade());
                assertEquals(analyticsElementFromFeature.getGrade().getTimeSpent(), analyticsElementFromResponse.getGrade().getTimeSpent());
                assertEquals(analyticsElementFromFeature.getStartDate(), analyticsElementFromResponse.getStartDate());
                analyticsElementsFromResponse.remove(analyticsElementFromResponse);
                break;
            }
        }
    }

    @Then("ratings should be")
    public void ratingModelsShouldBe(List<Rating> ratings) {
        List<Rating> ratingsListFromResponse = (List<Rating>) getTestExecutionContext().getResponse().getBody();
        for (Rating rating : ratings) {
            for (Rating ratingFromResponse : ratingsListFromResponse) {
                assertEquals(rating.getStudent().getId(), ratingFromResponse.getStudent().getId());
                assertEquals(rating.getGrade().getGrade(), ratingFromResponse.getGrade().getGrade());
                assertEquals(rating.getGrade().getTimeSpent(), ratingFromResponse.getGrade().getTimeSpent());
                ratingsListFromResponse.remove(ratingFromResponse);
                break;
            }
        }
    }
}