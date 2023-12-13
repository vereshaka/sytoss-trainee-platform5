package com.sytoss.lessons.controllers.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamSummaryStatistic {

    private Long id;

    private String name;

    private Integer maxGrade;

    private Integer studentMaxGrade;

}
