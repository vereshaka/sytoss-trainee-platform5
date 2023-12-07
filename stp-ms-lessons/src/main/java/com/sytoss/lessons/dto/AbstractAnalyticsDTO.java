package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAnalyticsDTO {
    protected Long studentId;
    protected Long rank;
}
