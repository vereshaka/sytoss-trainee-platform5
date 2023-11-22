package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnalyticsAverageDTO {
    private Long studentId;
    private Double avgGrade;
    private Long avgTimeSpent;
    public AnalyticsAverageDTO(Long studentId, Double avgGrade, Double avgTimeSpent) {
        this.studentId = studentId;
        this.avgGrade = avgGrade;
        this.avgTimeSpent = Math.round(avgTimeSpent);
    }
}