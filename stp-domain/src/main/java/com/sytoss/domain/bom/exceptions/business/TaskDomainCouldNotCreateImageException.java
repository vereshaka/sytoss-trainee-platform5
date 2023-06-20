package com.sytoss.domain.bom.exceptions.business;

public class TaskDomainCouldNotCreateImageException extends BusinessException {

    public TaskDomainCouldNotCreateImageException() {
        super("Image have been not created");
    }
}
