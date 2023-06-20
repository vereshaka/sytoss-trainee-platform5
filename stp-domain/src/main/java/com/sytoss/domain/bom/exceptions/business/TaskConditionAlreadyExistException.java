package com.sytoss.domain.bom.exceptions.business;

public class TaskConditionAlreadyExistException extends AlreadyExistException {

    public TaskConditionAlreadyExistException(String value) {
        super("TaskCondition", value);
    }
}
