package com.sytoss.lessons.bom;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticsModel {

    private Discipline discipline;
    private Student student;
    private Exam exam;
    private PersonalExam personalExam;
    private Double avgGrade;
    private Long avgTimeSpent;
}
