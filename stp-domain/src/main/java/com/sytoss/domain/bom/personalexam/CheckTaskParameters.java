package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.lessons.TaskCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CheckTaskParameters extends CheckRequestParameters{

    private String etalon;

    private List<TaskCondition> conditions = new ArrayList<>();
}
