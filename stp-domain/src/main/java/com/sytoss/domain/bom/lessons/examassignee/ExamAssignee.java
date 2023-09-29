package com.sytoss.domain.bom.lessons.examassignee;

import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class ExamAssignee {

    private Date relevantFrom;

    private Date relevantTo;

    private Integer duration;
}
