package com.sytoss.domain.bom.exceptions.business;

public class TaskDomainAlreadyExist extends AlreadyExistException{

    public TaskDomainAlreadyExist(String name) {
        super("TaskDomain", name);
    }
}
