package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Task;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Answer {

    private Long id;

    @JsonView(PersonalExam.Public.class)
    private String value;

    @JsonView(PersonalExam.Public.class)
    private Task task;

    @JsonView(PersonalExam.Public.class)
    private AnswerStatus status;

    @JsonView(PersonalExam.Public.class)
    private Grade grade;
}
