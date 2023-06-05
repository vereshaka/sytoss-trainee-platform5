package com.sytoss.domain.bom.exceptions.business;

public class TaskExistException extends BusinessException {

    public TaskExistException(String question) {
        super("Task with question \"" + question + "\" already exist");
    }
}
