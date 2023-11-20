package com.sytoss.lessons.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsElementId implements Serializable {
    private Long disciplineId;

    private Long examId;

    private Long studentId;
}