package com.sytoss.domain.bom.exceptions.businessException;

public class DisciplineNotFoundException extends NotFoundException {

    public DisciplineNotFoundException(Long id) {
        super("Discipline", id);
    }

}
