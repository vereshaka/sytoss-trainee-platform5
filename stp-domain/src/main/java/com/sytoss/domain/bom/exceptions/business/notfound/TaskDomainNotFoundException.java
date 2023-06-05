package com.sytoss.domain.bom.exceptions.business.notfound;

public class TaskDomainNotFoundException extends NotFoundException {

    public TaskDomainNotFoundException(Long id) {
        super("Task Domain", id);
    }
}
