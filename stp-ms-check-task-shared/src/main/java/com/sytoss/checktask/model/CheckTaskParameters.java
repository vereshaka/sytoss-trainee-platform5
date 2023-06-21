package com.sytoss.checktask.model;

import com.sytoss.domain.bom.lessons.TaskCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CheckTaskParameters {

    private String answer;

    private String etalon;

    private String script;

    private List<TaskCondition> conditions = new ArrayList<>();
}
