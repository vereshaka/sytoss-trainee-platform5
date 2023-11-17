package com.sytoss.domain.bom.lessons.analytics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingModel {
    private Long studentId;
    private Double avgGrade;
    private Long avgTimeSpent;

    public RatingModel(Long studentId, Double avgGrade, Double avgTimeSpent) {
        this.studentId = studentId;
        this.avgGrade = avgGrade;
        this.avgTimeSpent = Math.round(avgTimeSpent);
    }
}
