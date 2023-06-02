package com.sytoss.domain.bom.exceptions.business.notfound;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task", id);
    }
}
