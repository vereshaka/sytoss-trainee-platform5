package com.sytoss.domain.bom.exceptions.businessException;

public class AlreadyExistException extends BusinessException {

    public AlreadyExistException(String objectName, String name) {

        super(objectName + " with name \"" + name + "\" already exist");
    }
}
