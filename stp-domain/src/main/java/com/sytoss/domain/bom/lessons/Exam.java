package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Exam {

    private Long id;

    private Date relevantFrom;

    private Date relevantTo;

    private Integer duration;

    private Long groupId;

    private Integer numberOfTasks;

    private List<Topic> topics;
}
