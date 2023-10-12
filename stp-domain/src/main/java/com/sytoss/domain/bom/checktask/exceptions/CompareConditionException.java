package com.sytoss.domain.bom.checktask.exceptions;

import com.sytoss.domain.bom.lessons.TaskCondition;
import lombok.Getter;

import java.util.List;

@Getter
public class CompareConditionException extends RuntimeException {

    private final List<TaskCondition> failedCondition;

    public CompareConditionException(List<TaskCondition> failedCondition) {
        super(String.join(";", failedCondition.stream().map(TaskCondition::getCondition).toList()) + " condition are failed to check");
        this.failedCondition = failedCondition;
    }
}
