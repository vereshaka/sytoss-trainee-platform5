package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.analytics.AnaliticGrade;
import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.Grade;
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
        Analytic analyticFromResponse = getTestExecutionContext().getDetails().getAnalytic();

        Map<String,String> analyticsMap = dataTable.asMaps().get(0);
        Analytic analytic = new Analytic();
        Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
        Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
        Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
        String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");
        Exam exam = new Exam();
        exam.setId(examId);
        analytic.setExam(exam);
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        analytic.setDiscipline(discipline);
        Student student = new Student();
        student.setId(studentId);
        analytic.setStudent(student);
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(personalExamId);

        AnaliticGrade grade = new AnaliticGrade();
        if (analyticsMap.get("grade") != null) {
            grade.setGrade(Double.parseDouble(analyticsMap.get("grade").trim()));
        }
        if (analyticsMap.get("timeSpent") != null) {
            grade.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
        }
        analytic.setGrade(grade);

        assertEquals(analyticFromResponse.getGrade().getGrade(), analytic.getGrade().getGrade());
    }

    @Then("analytics elements should be")
    public void analyticsShouldBe(DataTable dataTable) throws ParseException {
        List<Analytic> analyticsElementsFromResponse = (List<Analytic>) getTestExecutionContext().getResponse().getBody();
        List<Map<String, String>> analyticsMapList = dataTable.asMaps();
        List<Analytic> analyticsElementList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        for (Map<String, String> analyticsMap : analyticsMapList) {
            Analytic analytic = new Analytic();
            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("disciplineId").trim()).toString());
            Long examId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examId").trim()).toString());
            Long studentId = Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("studentId").trim()).toString());
            String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");
            Exam exam = new Exam();
            exam.setId(examId);
            analytic.setExam(exam);
            Discipline discipline = new Discipline();
            discipline.setId(disciplineId);
            analytic.setDiscipline(discipline);
            Student student = new Student();
            student.setId(studentId);
            analytic.setStudent(student);
            PersonalExam personalExam = new PersonalExam();
            personalExam.setId(personalExamId);
            analytic.setPersonalExam(personalExam);

            if (analyticsMap.get("examAssigneeId") != null) {
                ExamAssignee examAssignee = new ExamAssignee();
                examAssignee.setId(Long.parseLong(getTestExecutionContext().replaceId(analyticsMap.get("examAssigneeId").trim()).toString()));
                analytic.getExam().getExamAssignees().add(examAssignee);
            }
            AnaliticGrade grade = new AnaliticGrade();
            if (analyticsMap.get("grade") != null) {
                grade.setGrade(Double.parseDouble(analyticsMap.get("grade").trim()));
            }
            if (analyticsMap.get("timeSpent") != null) {
                grade.setTimeSpent(Long.parseLong(analyticsMap.get("timeSpent").trim()));
            }
            if (analyticsMap.get("startDate") != null) {
                analytic.setStartDate(sdf.parse(analyticsMap.get("startDate").trim()));
            }
            analytic.setGrade(grade);
            analyticsElementList.add(analytic);
        }
        assertEquals(analyticsElementList.size(), analyticsElementsFromResponse.size());
        for (Analytic analyticsElementFromFeature : analyticsElementList) {
            for (Analytic analyticsElementFromResponse : analyticsElementsFromResponse) {
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
}