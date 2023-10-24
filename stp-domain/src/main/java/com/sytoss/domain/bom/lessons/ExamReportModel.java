package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamReportModel {

    private String examName;

    private Integer maxGrade;

    private Date relevantFrom;

    private Integer amountOfTasks;

    private Date relevantTo;

    private List<TaskReportModel> tasks;
}
