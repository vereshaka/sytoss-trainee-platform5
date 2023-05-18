package com.sytoss.traineeplatform.bom.personalexam;

import com.sytoss.traineeplatform.bom.lessons.Task;
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
}
