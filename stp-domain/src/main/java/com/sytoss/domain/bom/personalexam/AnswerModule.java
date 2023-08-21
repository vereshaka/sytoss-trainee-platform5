package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AnswerModule {

    private String answer;

    private Date answerUIDate;

    private Long timeSpent;

    private Long taskId;
}
