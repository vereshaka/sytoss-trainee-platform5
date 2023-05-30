package com.sytoss.domain.bom.exceptions.business.notfound;

public class DisciplineNotFoundException extends NotFoundException {

    public DisciplineNotFoundException(Long id) {
        super("Discipline", id);
    }

}
