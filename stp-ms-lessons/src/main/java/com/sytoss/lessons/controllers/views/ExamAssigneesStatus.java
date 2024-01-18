package com.sytoss.lessons.controllers.views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamAssigneesStatus {

    private boolean inProgress;

    private boolean notStarted;

    private Integer taskCount;

    private Integer numberOfTask;
}