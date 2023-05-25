package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.exceptions.businessException.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.exceptions.businessException.PersonalExamIsFinishedException;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonalExam {

    @MongoId
    private String id;

    @JsonView({PersonalExam.Public.class})
    private String name;

    private Discipline discipline;

    @JsonView(PersonalExam.Public.class)
    private Date date;

    @JsonView(PersonalExam.Public.class)
    private Long studentId;

    @JsonView(PersonalExam.Public.class)
    private List<Answer> answers = new ArrayList<>();

    private Integer time;

    private Integer amountOfTasks;

    private PersonalExamStatus status;

    @JsonView(PersonalExam.Public.class)
    private float summaryGrade;

    public void start() {
        if (status.equals(PersonalExamStatus.NOT_STARTED)) {
            status = PersonalExamStatus.IN_PROGRESS;
        } else if (status.equals(PersonalExamStatus.IN_PROGRESS)) {
            throw new PersonalExamAlreadyStartedException();
        } else {
            throw new PersonalExamIsFinishedException();
        }
    }

    public void summary() {
        summaryGrade = 0;

        answers.forEach((answer) -> {
            if (answer.getStatus().equals(AnswerStatus.GRADED)) {
                summaryGrade += answer.getGrade().getValue();
            }
        });
    }

    public static class Public {
        public static class StartExam {

        }
    }
}