package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.controllers.views.*;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableConfigurator extends LessonsIntegrationTest {

    @DataTableType
    public TopicDTO mapGroupDTO(Map<String, String> row) {
        TopicDTO result = new TopicDTO();
        result.setName(row.get("topic"));
        result.setDiscipline(new DisciplineDTO());
        result.getDiscipline().setName(row.get("discipline"));
        return result;
    }

    @DataTableType
    public StudentDisciplineStatistic mapAnalyticFull(Map<String, String> row) {
        StudentDisciplineStatistic studentDisciplineStatistic = new StudentDisciplineStatistic();
        Discipline discipline = new Discipline();
        discipline.setId((Long) getTestExecutionContext().replaceId(row.get("disciplineId")));
        studentDisciplineStatistic.setDiscipline(discipline);
        Student student = new Student();
        student.setId(Long.parseLong((String) getTestExecutionContext().replaceId(row.get("studentId"))));
        studentDisciplineStatistic.setStudent(student);

        SummaryGrade summaryGrade = new SummaryGrade();

        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(Double.parseDouble(row.get("average grade")));
        averageGrade.setTimeSpent(Long.parseLong(row.get("average spent time")));
        summaryGrade.setAverage(averageGrade);

        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(Double.parseDouble(row.get("max grade")));
        maxGrade.setTimeSpent(Long.parseLong(row.get("min spent time")));
        summaryGrade.setMax(maxGrade);

        studentDisciplineStatistic.setSummaryGrade(summaryGrade);

        return studentDisciplineStatistic;
    }

    @DataTableType
    public StudentTestExecutionSummary mapTest(Map<String, String> row) {

        StudentTestExecutionSummary test = new StudentTestExecutionSummary();

        ExamSummaryStatistic exam = new ExamSummaryStatistic();
        exam.setId((Long) getTestExecutionContext().replaceId(row.get("examId")));
        exam.setName(row.get("examName"));
        exam.setStudentMaxGrade(Integer.parseInt(row.get("examMaxGrade")));

        PersonalExamSummaryStatistic personalExam = new PersonalExamSummaryStatistic();
        personalExam.setGrade(Double.parseDouble(row.get("personalExamGrade")));
        personalExam.setSpentTime(Long.parseLong(row.get("personalExamSpentTime")));
        personalExam.setPersonalExamId((String) getTestExecutionContext().replaceId(row.get("personalExamId")));

        test.setPersonalExam(personalExam);
        test.setExam(exam);

        return test;
    }

    @DataTableType
    public DisciplineSummary mapDisciplineSummary(Map<String, String> row) {
        DisciplineSummary disciplineSummary = new DisciplineSummary();

        SummaryGrade studentsGrade = new SummaryGrade();
        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(Double.parseDouble(row.get("average grade")));
        averageGrade.setTimeSpent(Long.parseLong(row.get("average spent time")));
        studentsGrade.setAverage(averageGrade);

        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(Double.parseDouble(row.get("max grade")));
        maxGrade.setTimeSpent(Long.parseLong(row.get("min spent time")));
        studentsGrade.setMax(maxGrade);

        disciplineSummary.setStudentsGrade(studentsGrade);

        return disciplineSummary;
    }

    @DataTableType
    public ExamSummary mapExamSummary(Map<String, String> row) {
        ExamSummary examSummary = new ExamSummary();
        Exam exam = new Exam();
        exam.setId((Long) getTestExecutionContext().replaceId(row.get("exam id")));

        SummaryGrade studentsGrade = new SummaryGrade();
        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(Double.parseDouble(row.get("students average grade")));
        averageGrade.setTimeSpent(Long.parseLong(row.get("students average spent time")));
        studentsGrade.setAverage(averageGrade);

        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(Double.parseDouble(row.get("max students grade")));
        maxGrade.setTimeSpent(Long.parseLong(row.get("min students spent time")));
        studentsGrade.setMax(maxGrade);

        examSummary.setExam(exam);
        examSummary.setStudentsGrade(studentsGrade);

        return examSummary;
    }

}
