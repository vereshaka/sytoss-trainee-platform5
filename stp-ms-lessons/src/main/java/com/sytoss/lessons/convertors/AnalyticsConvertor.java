package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.analytics.AnaliticGrade;
import com.sytoss.domain.bom.analytics.Analytic;
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
    public void fromDTO(AnalyticsDTO source, Analytic destination) {
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
        AnaliticGrade analiticGrade = new AnaliticGrade();
        analiticGrade.setGrade(source.getGrade());
        analiticGrade.setTimeSpent(source.getTimeSpent());
        destination.setGrade(analiticGrade);
    }

    public void toDTO(Analytic source, AnalyticsDTO destination) {
        if (source != null) {
            destination.setDisciplineId(source.getDiscipline().getId());
            destination.setExamId(source.getExam().getId());
            destination.setStudentId(source.getStudent().getId());
            destination.setPersonalExamId(source.getPersonalExam().getId());
            destination.setGrade(source.getGrade().getGrade());
            destination.setTimeSpent(source.getGrade().getTimeSpent());
        }
    }
}
