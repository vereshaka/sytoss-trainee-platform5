package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ExamModel {

    private String name;

    private Integer time;

    private Integer amountOfTasks;

    private Date relevantTo;
}
