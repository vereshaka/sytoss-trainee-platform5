package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.exceptions.businessException.AnswerInProgressException;
import com.sytoss.domain.bom.exceptions.businessException.AnswerIsAnsweredException;
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

    public void inProgress() {
        if (this.status.equals(AnswerStatus.NOT_STARTED)) {
            status = AnswerStatus.IN_PROGRESS;
        } else if (this.status.equals(AnswerStatus.IN_PROGRESS)) {
            throw new AnswerInProgressException();
        } else {
            throw new AnswerIsAnsweredException();
        }
    }
}
