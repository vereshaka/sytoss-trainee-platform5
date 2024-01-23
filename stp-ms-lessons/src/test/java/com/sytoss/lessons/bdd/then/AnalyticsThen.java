package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.given.AbstractGiven;
import com.sytoss.lessons.controllers.views.*;
import com.sytoss.lessons.dto.AnalyticsDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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

        assertEquals(analytics.getGrade().getGrade(),analyticsFromResponse.getGrade().getGrade());
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

    @Then("full discipline summary should has values")
    public void fullDisciplineSummaryShouldHasValues(DisciplineSummary expectedDisciplineSummary) {
        DisciplineGroupSummary disciplineSummary = (DisciplineGroupSummary) getTestExecutionContext().getResponse().getBody();

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

    @Then("exam summaries by exams should be")
    public void examSummariesByExamsShouldBe(DataTable expectedExamSummaries) {
        DisciplineGroupSummary disciplineSummaryFromFeature = new DisciplineGroupSummary();

        List<Map<String, String>> rows = expectedExamSummaries.asMaps();

        for (Map<String, String> row : rows) {
            Long examId = (Long) getTestExecutionContext().replaceId(row.get("exam id"));

            SummaryGrade studentsGrade = new SummaryGrade();
            AnalyticGrade averageGrade = new AnalyticGrade();
            averageGrade.setGrade(Double.parseDouble(row.get("students average grade")));
            averageGrade.setTimeSpent(Long.parseLong(row.get("students average spent time")));
            studentsGrade.setAverage(averageGrade);

            AnalyticGrade maxGrade = new AnalyticGrade();
            maxGrade.setGrade(Double.parseDouble(row.get("max students grade")));
            maxGrade.setTimeSpent(Long.parseLong(row.get("min students spent time")));
            studentsGrade.setMax(maxGrade);

            Exam exam = new Exam();
            exam.setId(examId);

            ExamGroupSummary examGroupSummary = new ExamGroupSummary();
            examGroupSummary.setExam(exam);
            examGroupSummary.setStudentsGrade(studentsGrade);
        }

        DisciplineGroupSummary disciplineSummary = (DisciplineGroupSummary) getTestExecutionContext().getResponse().getBody();

        assert disciplineSummary != null;

        for (ExamGroupSummary expectedExamSummary : disciplineSummaryFromFeature.getTests()) {
            Optional<ExamGroupSummary> examSummary = disciplineSummary.getTests().stream().filter(test ->
                            test.getExam().getId().equals(expectedExamSummary.getExam().getId())
                                    && test.getStudentsGrade().getMax().getGrade() == expectedExamSummary.getStudentsGrade().getMax().getGrade()
                                    && test.getStudentsGrade().getMax().getTimeSpent() == expectedExamSummary.getStudentsGrade().getMax().getTimeSpent()
                                    && test.getStudentsGrade().getAverage().getGrade() == expectedExamSummary.getStudentsGrade().getAverage().getGrade()
                                    && test.getStudentsGrade().getAverage().getTimeSpent() == expectedExamSummary.getStudentsGrade().getAverage().getTimeSpent()
                    )
                    .findFirst();
            if(examSummary.isEmpty()){
                log.error("Doesn't find appropriate value for"+expectedExamSummary.getExam().getId());
            }
            assertTrue(examSummary.isPresent());
        }
    }

    @Then("exam summaries by groups should be")
    public void examSummariesByGroupsShouldBe(DataTable expectedExamSummaries) {
        DisciplineGroupSummary disciplineSummaryFromFeature = new DisciplineGroupSummary();

        List<Map<String, String>> rows = expectedExamSummaries.asMaps();

        for (Map<String, String> row : rows) {
            Long examId = (Long) getTestExecutionContext().replaceId(row.get("exam id"));

            Group group = new Group();
            group.setId(Long.parseLong(row.get("group id")));

            SummaryGrade studentsGrade = new SummaryGrade();
            AnalyticGrade averageGrade = new AnalyticGrade();
            averageGrade.setGrade(Double.parseDouble(row.get("students average grade")));
            averageGrade.setTimeSpent(Long.parseLong(row.get("students average spent time")));
            studentsGrade.setAverage(averageGrade);

            AnalyticGrade maxGrade = new AnalyticGrade();
            maxGrade.setGrade(Double.parseDouble(row.get("max students grade")));
            maxGrade.setTimeSpent(Long.parseLong(row.get("min students spent time")));
            studentsGrade.setMax(maxGrade);

            if (disciplineSummaryFromFeature.getTests()!=null && disciplineSummaryFromFeature.getTests().stream().map(ExamGroupSummary::getExam).map(Exam::getId).toList().contains(examId)) {
                ExamGroupSummary examSummary = disciplineSummaryFromFeature.getTests().stream().filter(examSummary1 -> Objects.equals(examSummary1.getExam().getId(), examId)).toList().get(0);
                int index = disciplineSummaryFromFeature.getTests().indexOf(examSummary);
                Map<Long,SummaryGrade> map = new HashMap<>(examSummary.getGradesByGroup());
                map.put(group.getId(), studentsGrade);
                examSummary.setGradesByGroup(map);
                disciplineSummaryFromFeature.getTests().set(index,examSummary);
            } else {
                ExamGroupSummary examSummary = new ExamGroupSummary();
                Exam exam = new Exam();
                exam.setId(examId);
                examSummary.setExam(exam);
                examSummary.setGradesByGroup(Map.of(group.getId(), studentsGrade));
                disciplineSummaryFromFeature.getTests().add(examSummary);
            }
        }

        DisciplineGroupSummary disciplineSummary = (DisciplineGroupSummary) getTestExecutionContext().getResponse().getBody();

        assert disciplineSummary != null;

        for (ExamGroupSummary expectedExamSummary : disciplineSummaryFromFeature.getTests()) {
            for (Long groupId : expectedExamSummary.getGradesByGroup().keySet()) {
                Optional<ExamGroupSummary> examSummary = disciplineSummary.getTests().stream().filter(test ->
                                test.getExam().getId().equals(expectedExamSummary.getExam().getId())
                                        && test.getGradesByGroup().get(groupId).getMax().getGrade() == expectedExamSummary.getGradesByGroup().get(groupId).getMax().getGrade()
                                        && test.getGradesByGroup().get(groupId).getMax().getTimeSpent() == expectedExamSummary.getGradesByGroup().get(groupId).getMax().getTimeSpent()
                                        && test.getGradesByGroup().get(groupId).getAverage().getGrade() == expectedExamSummary.getGradesByGroup().get(groupId).getAverage().getGrade()
                                        && test.getGradesByGroup().get(groupId).getAverage().getTimeSpent() == expectedExamSummary.getGradesByGroup().get(groupId).getAverage().getTimeSpent()
                        )
                        .findFirst();
                if(examSummary.isEmpty()){
                    log.error("Doesn't find appropriate value for"+expectedExamSummary.getExam().getId()+" and group "+groupId);
                }
                assertTrue(examSummary.isPresent());
            }
        }
    }
}
