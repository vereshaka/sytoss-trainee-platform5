package com.sytoss.domain.bom.lessons.examassignee;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class ExamAssignee {
    private Long id;

    private Date relevantFrom;

    private Date relevantTo;

    private Integer duration;
}
