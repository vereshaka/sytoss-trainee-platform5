package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.lessons.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstTask {

    private Task task;

    private Integer time;

    private Integer amountOfTasks;

    private String name;
}
