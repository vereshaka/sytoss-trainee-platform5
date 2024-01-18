package com.sytoss.domain.bom.exceptions.business;

public class TaskAlreadyExistException extends BusinessException {

    public TaskAlreadyExistException(String fieldName, String value) {
        super("Task with "+fieldName+" \"" + value + "\" already exist");
    }
}
