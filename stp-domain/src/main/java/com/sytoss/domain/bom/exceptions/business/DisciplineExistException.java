package com.sytoss.domain.bom.exceptions.business;

public class DisciplineExistException extends AlreadyExistException {
    public DisciplineExistException(String name) {
        super("Discipline", name);
    }
}
