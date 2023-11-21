package com.sytoss.lessons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "ANALYTICS")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_ANALYTICS", columnNames = {"DISCIPLINE_ID", "EXAM_ID", "STUDENT_ID"})})
public class AnalyticsDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analytics_id_generator")
    @SequenceGenerator(name = "analytics_id_generator", sequenceName = "ANALYTICS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "DISCIPLINE_ID")
    private Long disciplineId;

    @Column(name = "EXAM_ID")
    private Long examId;

    @Column(name = "STUDENT_ID")
    private Long studentId;

    @Column(name = "PERSONAL_EXAM_ID")
    private String personalExamId;

    @Column(name = "GRADE")
    private Double grade = 0.0;

    @Column(name = "TIME_SPENT")
    private Long timeSpent = 0L;

    @Column(name = "START_DATE")
    private Date startDate;
}
