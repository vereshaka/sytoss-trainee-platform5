package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCondition {

    private Long id;

    private String name;

    private ConditionType type;
}
