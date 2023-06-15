package com.sytoss.domain.bom.exceptions.business;

public class TaskDomainIsUsed extends BusinessException {

    public TaskDomainIsUsed(String name) {
        super("Task domain with " + name + " because personal exam not finished");
    }
}
