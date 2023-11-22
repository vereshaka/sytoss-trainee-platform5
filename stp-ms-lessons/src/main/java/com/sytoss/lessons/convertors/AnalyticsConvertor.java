package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.AnalyticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsConvertor {

    public void fromDTO(AnalyticsDTO source, Analytics destination) {
        Discipline discipline = new Discipline();
        discipline.setId(source.getDisciplineId());
        destination.setDiscipline(discipline);
        Exam exam = new Exam();
        exam.setId(source.getExamId());
        destination.setExam(exam);
        Student student = new Student();
        student.setId(source.getStudentId());
        destination.setStudent(student);
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(source.getPersonalExamId());
        destination.setPersonalExam(personalExam);
        AnalyticGrade analyticGrade = new AnalyticGrade();
        analyticGrade.setGrade(source.getGrade());
        analyticGrade.setTimeSpent(source.getTimeSpent());
        destination.setGrade(analyticGrade);
    }

    public void toDTO(Analytics source, AnalyticsDTO destination) {
        destination.setDisciplineId(source.getDiscipline().getId());
        destination.setExamId(source.getExam().getId());
        destination.setStudentId(source.getStudent().getId());
        destination.setPersonalExamId(source.getPersonalExam().getId());
        destination.setGrade(source.getGrade().getGrade());
        destination.setTimeSpent(source.getGrade().getTimeSpent());
    }
}
