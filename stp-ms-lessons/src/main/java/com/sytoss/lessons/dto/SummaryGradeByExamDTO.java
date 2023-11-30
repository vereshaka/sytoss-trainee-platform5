package com.sytoss.lessons.dto;

import com.sytoss.domain.bom.lessons.Exam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryGradeByExamDTO extends SummaryGradeDTO {

    private Long examId;
    private Integer examMaxGrade;
    private String name;

    public SummaryGradeByExamDTO(Double maxGrade, Long maxTimeSpent, Double avgGrade, Long avgTimeSpent, Long examId, Integer examMaxGrade, String name) {
        super(maxGrade, maxTimeSpent, avgGrade, avgTimeSpent);

        this.examId = examId;
        this.examMaxGrade = examMaxGrade;
        this.name = name;
    }

}
