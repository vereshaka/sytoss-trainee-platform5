package com.sytoss.lessons.controllers.views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamAssigneesStatus {
    private Boolean inProgress;
    private Boolean notStarted;
    private Integer taskCount;
    private Integer numberOfTask;
}