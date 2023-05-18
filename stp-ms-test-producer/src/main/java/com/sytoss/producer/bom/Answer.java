package com.sytoss.producer.bom;

import com.fasterxml.jackson.annotation.JsonView;
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

    private AnswerStatus status;

    @JsonView(PersonalExam.Public.class)
    private Grade grade;
}
