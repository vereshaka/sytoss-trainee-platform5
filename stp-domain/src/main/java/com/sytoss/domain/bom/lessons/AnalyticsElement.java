package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticsElement {

    private Long disciplineId;

    private Long examId;

    private Long studentId;

    private String personalExamId;

    private Double grade;

    private Long timeSpent;

    private Long examAssigneeId;
}
