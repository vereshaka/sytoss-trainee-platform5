package com.sytoss.domain.bom.exceptions.business.notfound;

public class TaskConditionNotFoundException extends NotFoundException {

    public TaskConditionNotFoundException(Long id) {
        super("Condition", id);
    }
}
