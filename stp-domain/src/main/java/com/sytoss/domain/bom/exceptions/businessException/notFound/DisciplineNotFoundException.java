package com.sytoss.domain.bom.exceptions.businessException.notFound;

import com.sytoss.domain.bom.exceptions.businessException.BusinessException;

public class DisciplineNotFoundException extends BusinessException {

    public DisciplineNotFoundException(Long id){
        super("Discipline with id " + id + " not found!");
    }
}