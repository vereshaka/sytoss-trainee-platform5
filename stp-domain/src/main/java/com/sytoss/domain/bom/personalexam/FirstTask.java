package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstTask {

    @JsonView({PersonalExam.Public.StartExam.class})
    private Task task;

    @JsonView({PersonalExam.Public.StartExam.class})
    private Integer time;

    @JsonView({PersonalExam.Public.StartExam.class})
    private Integer amountOfTasks;

    @JsonView({PersonalExam.Public.StartExam.class})
    private String name;
}
