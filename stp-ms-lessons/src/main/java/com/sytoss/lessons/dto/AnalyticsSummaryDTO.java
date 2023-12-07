package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticsSummaryDTO extends AbstractAnalyticsDTO {
    private Double sumGrade;
    private Long sumTimeSpent;

    public AnalyticsSummaryDTO(Long studentId, Double sumGrade, Long sumTimeSpent, Long rank) {
        this.studentId = studentId;
        this.sumGrade = sumGrade;
        this.sumTimeSpent = sumTimeSpent;
        this.rank = rank;
    }
}
