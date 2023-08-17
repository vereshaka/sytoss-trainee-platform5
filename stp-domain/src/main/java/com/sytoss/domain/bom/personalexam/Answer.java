package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.exceptions.business.AnswerInProgressException;
import com.sytoss.domain.bom.exceptions.business.AnswerIsAnsweredException;
import com.sytoss.domain.bom.lessons.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Answer {

    @JsonView(PersonalExam.Public.class)
    private Long id;

    @JsonView(PersonalExam.Public.class)
    private String value;

    @JsonView({PersonalExam.Public.class})
    private Task task;

    @JsonView(PersonalExam.Public.class)
    private AnswerStatus status;

    @JsonView(PersonalExam.Public.class)
    private Grade grade;

    @JsonView(PersonalExam.Public.class)
    private Grade teacherGrade;

    private Date realDateAnswer;

    private Long timeSpent;

    public void inProgress() {
        if (this.status.equals(AnswerStatus.NOT_STARTED)) {
            status = AnswerStatus.IN_PROGRESS;
        } else if (this.status.equals(AnswerStatus.IN_PROGRESS)) {
            throw new AnswerInProgressException();
        } else {
            throw new AnswerIsAnsweredException();
        }
    }

    public void grade(Grade grade) {
        setGrade(grade);
        setTeacherGrade(grade);
        setStatus(AnswerStatus.GRADED);
    }

    public void answer(String value) {
        setValue(value);
        setStatus(AnswerStatus.ANSWERED);
    }
}