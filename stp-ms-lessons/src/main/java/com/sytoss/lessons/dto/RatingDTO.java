package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {

    private long rank;

    private Long studentId;

    private Double grade = 0.0;

    private Long timeSpent = 0L;
}
