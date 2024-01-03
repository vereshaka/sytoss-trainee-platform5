package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SummaryGradeByExamDTO extends SummaryGradeDTO {

    private Long examId;
    private Integer examMaxGrade;
    private String name;
    private Date creationDate;

    public SummaryGradeByExamDTO(Double maxGrade, Long maxTimeSpent, Double avgGrade, Long avgTimeSpent, Long examId, Integer examMaxGrade, String name, Date creationDate) {
        super(maxGrade, maxTimeSpent, avgGrade, avgTimeSpent);

        this.examId = examId;
        this.examMaxGrade = examMaxGrade;
        this.name = name;
        this.creationDate = creationDate;
    }

}
