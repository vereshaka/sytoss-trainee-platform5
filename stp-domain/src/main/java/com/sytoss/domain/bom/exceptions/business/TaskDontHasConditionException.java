package com.sytoss.domain.bom.exceptions.business;

public class TaskDontHasConditionException extends BusinessException{

    public TaskDontHasConditionException(Long taskId, Long conditionId) {
        super("Task with id: " + taskId + "don't have Condition with id: " + conditionId);
    }
}
