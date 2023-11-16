package com.sytoss.lessons.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "ANALYTICS")
@IdClass(AnalyticsElementId.class)
public class AnalyticsElementDTO extends Auditable {

    @Id
    @Column(name = "DISCIPLINE_ID")
    private Long disciplineId;

    @Id
    @Column(name = "EXAM_ID")
    private Long examId;

    @Id
    @Column(name = "STUDENT_ID")
    private Long studentId;

    @Column(name = "PERSONAL_EXAM_ID")
    private String personalExamId;

    @Column(name = "GRADE")
    private Double grade;

    @Column(name = "TIME_SPENT")
    private Long timeSpent;
}
