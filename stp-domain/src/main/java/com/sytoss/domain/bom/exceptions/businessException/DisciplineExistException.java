package com.sytoss.domain.bom.exceptions.businessException;

public class DisciplineExistException extends AlreadyExistException {
    public DisciplineExistException(String name) {
        super("Discipline", name);
    }
}
