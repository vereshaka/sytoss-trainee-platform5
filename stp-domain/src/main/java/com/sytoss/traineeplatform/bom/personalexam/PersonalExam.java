package com.sytoss.traineeplatform.bom.personalexam;

import com.sytoss.traineeplatform.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonalExam {

    @Id
    private Long id;

    private String name;

    private Discipline discipline;

    private Date date;

    private Long studentId;

    private List<Answer> answers;

    private PersonalExamStatus status;

    private float summaryGrade;
}
