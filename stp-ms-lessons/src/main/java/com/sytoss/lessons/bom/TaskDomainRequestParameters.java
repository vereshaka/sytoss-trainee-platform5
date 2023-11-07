package com.sytoss.lessons.bom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDomainRequestParameters {
    private String request;
    private String checkAnswer;
    private Long taskDomainId;
}