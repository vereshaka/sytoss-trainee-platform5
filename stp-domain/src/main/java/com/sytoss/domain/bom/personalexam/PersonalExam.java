package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonalExam {

    private String id;

    private String name;

    private Discipline discipline;

    private Date date;

    private Long studentId;

    private List<Answer> answers;

    private PersonalExamStatus status;

    private float summaryGrade;
}
