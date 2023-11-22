package com.sytoss.domain.bom.analytics;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Analytic {

    private Discipline discipline;
    private Exam exam;
    private Student student;

    private PersonalExam personalExam;
    private AnaliticGrade grade;
    private Date startDate;
}
