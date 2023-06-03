package com.sytoss.domain.bom.exceptions.business;

public class GroupExistException extends AlreadyExistException {

    public GroupExistException(String name) {
        super("Group", name);
    }
}
