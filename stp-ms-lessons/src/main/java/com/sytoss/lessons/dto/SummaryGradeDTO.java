package com.sytoss.lessons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SummaryGradeDTO {

    private Double maxGrade;

    private Long maxTimeSpent;

    private Double avgGrade;

    private Long avgTimeSpent;

}
