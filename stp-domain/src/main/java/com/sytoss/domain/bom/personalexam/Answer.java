package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.lessons.Task;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Answer {

    private Long id;

    private String value;

    private Task task;

    private AnswerStatus status;

    private Grade grade;

    public void changeStatus() {
        if (status.equals(AnswerStatus.NOT_STARTED)) {
            status = AnswerStatus.IN_PROGRESS;
        } else if (status.equals(AnswerStatus.IN_PROGRESS)) {
            status = AnswerStatus.ANSWERED;
        }
    }
}
