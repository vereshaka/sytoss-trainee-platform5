package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Exam {

    private Long id;

    private String name;

    private Date relevantFrom;

    private Date relevantTo;

    private Integer duration;

    private Group group;

    private Integer numberOfTasks;

    private List<Topic> topics;

    private Teacher teacher;

    private List<Task> tasks;
}
