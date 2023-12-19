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
import com.sytoss.lessons.controllers.viewModel.DisciplineSummary;
import com.sytoss.lessons.controllers.viewModel.ExamSummary;
import com.sytoss.lessons.controllers.viewModel.StudentDisciplineStatistic;
import com.sytoss.lessons.controllers.viewModel.StudentTestExecutionSummary;
import com.sytoss.lessons.dto.AnalyticsDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsThen extends AbstractGiven {
    @Then("^updated analytic element should be$")
    public void gradeEquals(DataTable dataTable) {
        Analytics analyticsFromResponse = getTestExecutionContext().getDetails().getAnalytics();

        Map<String, String> analyticsMap = dataTable.asMaps().get(0);
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
        List<AnalyticsDTO> result = getAnalyticsConnector().findAllByDisciplineId(getTestExecutionContext().getDetails().getDisciplineId());
        List<Map<String, String>> analyticsMapList = dataTable.asMaps();
        List<Analytics> analyticsElementList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        for (Map<String, String> analyticsMap : analyticsMapList) {
            Analytics analytics = new Analytics();
            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
            Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
            Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
            String personalExamId = analyticsMap.get("personalExamId") == null ? null : analyticsMap.get("personalExamId").trim().replace("*", "");
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
            } else {
                grade.setGrade(0);
            }
            if (analyticsMap.get("timeSpent") != null) {
                grade.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
            } else {
                grade.setTimeSpent(0L);
            }
            if (analyticsMap.get("startDate") != null) {
                analytics.setStartDate(sdf.parse(analyticsMap.get("startDate").trim()));
            }
            analytics.setGrade(grade);
            analyticsElementList.add(analytics);
        }
        assertEquals(analyticsElementList.size(), result.size());
        for (Analytics analyticsElementFromFeature : analyticsElementList) {
            List<AnalyticsDTO> filterResult = result.stream().filter(dto -> Objects.equals(analyticsElementFromFeature.getDiscipline().getId(), dto.getDisciplineId()) &&
                    Objects.equals(analyticsElementFromFeature.getExam().getId(), dto.getExamId()) &&
                    Objects.equals(analyticsElementFromFeature.getStudent().getId(), dto.getStudentId()) &&
                    Objects.equals(analyticsElementFromFeature.getPersonalExam().getId(), dto.getPersonalExamId()) &&
                    Objects.equals(analyticsElementFromFeature.getGrade().getGrade(), dto.getGrade()) &&
                    Objects.equals(analyticsElementFromFeature.getGrade().getTimeSpent(), dto.getTimeSpent()) &&
                    Objects.equals(analyticsElementFromFeature.getStartDate(), dto.getStartDate())).toList();
            assertEquals(1, filterResult.size(), "Item# " + (analyticsElementList.size() - result.size()));
            result.remove(filterResult.get(0));
        }
    }

    @Then("ratings should be")
    public void ratingModelsShouldBe(List<Rating> ratings) {
        List<Rating> ratingsListFromResponse = (List<Rating>) getTestExecutionContext().getResponse().getBody();
        assertEquals(ratings.size(), ratingsListFromResponse.size());
        for (Rating rating : ratings) {
            for (Rating ratingFromResponse : ratingsListFromResponse) {
                assertEquals(rating.getStudent().getId(), ratingFromResponse.getStudent().getId());
                assertEquals(rating.getGrade().getGrade(), ratingFromResponse.getGrade().getGrade());
                assertEquals(rating.getGrade().getTimeSpent(), ratingFromResponse.getGrade().getTimeSpent());
                assertEquals(rating.getRank(), ratingFromResponse.getRank());
                ratingsListFromResponse.remove(ratingFromResponse);
                break;
            }
        }
    }

    @Then("StudentDisciplineStatistic object has to be returned")
    public void analyticFullObjectHasToBeReturned(StudentDisciplineStatistic expectedDisciplineStatistic) {

        StudentDisciplineStatistic disciplineStatistic = (StudentDisciplineStatistic) getTestExecutionContext().getResponse().getBody();

        assert disciplineStatistic != null;
        assertEquals(expectedDisciplineStatistic.getDiscipline().getId(), disciplineStatistic.getDiscipline().getId());
        assertEquals(expectedDisciplineStatistic.getStudent().getId(), disciplineStatistic.getStudent().getId());
        assertEquals(expectedDisciplineStatistic.getSummaryGrade().getAverage().getGrade(), disciplineStatistic.getSummaryGrade().getAverage().getGrade());
        assertEquals(expectedDisciplineStatistic.getSummaryGrade().getAverage().getTimeSpent(), disciplineStatistic.getSummaryGrade().getAverage().getTimeSpent());
        assertEquals(expectedDisciplineStatistic.getSummaryGrade().getMax().getGrade(), disciplineStatistic.getSummaryGrade().getMax().getGrade());
        assertEquals(expectedDisciplineStatistic.getSummaryGrade().getMax().getTimeSpent(), disciplineStatistic.getSummaryGrade().getMax().getTimeSpent());
    }

    @Then("StudentDisciplineStatistic should has tests")
    public void analyticFullShouldHasTests(List<StudentTestExecutionSummary> expectedTests) {
        StudentDisciplineStatistic studentDisciplineStatistic = (StudentDisciplineStatistic) getTestExecutionContext().getResponse().getBody();

        assert studentDisciplineStatistic != null;
        assertEquals(expectedTests.size(), studentDisciplineStatistic.getTests().size());

        for (StudentTestExecutionSummary expectedTest : expectedTests) {
            Optional<StudentTestExecutionSummary> testOptional = studentDisciplineStatistic.getTests().stream()
                    .filter(test -> test.getExam().getId().equals(expectedTest.getExam().getId())
                            && test.getExam().getName().equals(expectedTest.getExam().getName())
                            && test.getExam().getStudentMaxGrade().equals(expectedTest.getExam().getStudentMaxGrade())
                            && test.getPersonalExam().getGrade().equals(expectedTest.getPersonalExam().getGrade())
                            && test.getPersonalExam().getSpentTime().equals(expectedTest.getPersonalExam().getSpentTime())
                    ).findFirst();
            assertTrue(testOptional.isPresent());
        }
    }

    @Then("discipline summary should has values")
    public void disciplineSummaryShouldHasValues(DisciplineSummary expectedDisciplineSummary) {
        DisciplineSummary disciplineSummary = (DisciplineSummary) getTestExecutionContext().getResponse().getBody();

        assert disciplineSummary != null;

        assertEquals(expectedDisciplineSummary.getStudentsGrade().getMax().getGrade(), disciplineSummary.getStudentsGrade().getMax().getGrade());
        assertEquals(expectedDisciplineSummary.getStudentsGrade().getMax().getTimeSpent(), disciplineSummary.getStudentsGrade().getMax().getTimeSpent());
        assertEquals(expectedDisciplineSummary.getStudentsGrade().getAverage().getGrade(), disciplineSummary.getStudentsGrade().getAverage().getGrade());
        assertEquals(expectedDisciplineSummary.getStudentsGrade().getAverage().getTimeSpent(), disciplineSummary.getStudentsGrade().getAverage().getTimeSpent());
    }

    @Then("exam summaries should be")
    public void examSummariesShouldBe(List<ExamSummary> expectedExamSummaries) {
        DisciplineSummary disciplineSummary = (DisciplineSummary) getTestExecutionContext().getResponse().getBody();

        assert disciplineSummary != null;

        for (ExamSummary expectedExamSummary : expectedExamSummaries) {
            Optional<ExamSummary> examSummary = disciplineSummary.getTests().stream().filter(test ->
                            test.getExam().getId().equals(expectedExamSummary.getExam().getId())
                                    && test.getStudentsGrade().getMax().getGrade() == expectedExamSummary.getStudentsGrade().getMax().getGrade()
                                    && test.getStudentsGrade().getMax().getTimeSpent() == expectedExamSummary.getStudentsGrade().getMax().getTimeSpent()
                                    && test.getStudentsGrade().getAverage().getGrade() == expectedExamSummary.getStudentsGrade().getAverage().getGrade()
                                    && test.getStudentsGrade().getAverage().getTimeSpent() == expectedExamSummary.getStudentsGrade().getAverage().getTimeSpent()
                    )
                    .findFirst();
            assertTrue(examSummary.isPresent());
        }
    }
}
