package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.AnalyticFull;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.analytics.Test;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
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
    public AnalyticFull mapAnalyticFull(Map<String, String> row) {
        AnalyticFull analyticFull = new AnalyticFull();
        Discipline discipline = new Discipline();
        discipline.setId((Long) getTestExecutionContext().replaceId(row.get("disciplineId")));
        analyticFull.setDiscipline(discipline);
        Student student = new Student();
        student.setId(Long.parseLong((String) getTestExecutionContext().replaceId(row.get("studentId"))));
        analyticFull.setStudent(student);

        SummaryGrade summaryGrade = new SummaryGrade();

        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(Double.parseDouble(row.get("average grade")));
        averageGrade.setTimeSpent(Long.parseLong(row.get("average spent time")));
        summaryGrade.setAverage(averageGrade);

        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(Double.parseDouble(row.get("max grade")));
        maxGrade.setTimeSpent(Long.parseLong(row.get("max spent time")));
        summaryGrade.setMax(maxGrade);

        analyticFull.setStudentGrade(summaryGrade);

        return analyticFull;
    }

    @DataTableType
    public Test mapTest(Map<String, String> row){

        Test test = new Test();

        Exam exam = new Exam();
        exam.setId((Long) getTestExecutionContext().replaceId(row.get("examId")));
        exam.setName(row.get("examName"));
        exam.setMaxGrade(Integer.parseInt(row.get("examMaxGrade")));

        PersonalExam personalExam = new PersonalExam();
        personalExam.setMaxGrade(Double.parseDouble(row.get("personalExamGrade")));
        personalExam.setSpentTime(Long.parseLong(row.get("personalExamSpentTime")));
        personalExam.setId((String) getTestExecutionContext().replaceId(row.get("personalExamId")));

        test.setPersonalExam(personalExam);
        test.setExam(exam);

        return test;
    }

}
