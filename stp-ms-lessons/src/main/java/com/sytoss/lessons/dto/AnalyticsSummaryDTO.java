package com.sytoss.lessons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryDTO {
    private Long studentId;
    private Double sumGrade;
    private Long sumTimeSpent;
    private Long rank;
}
