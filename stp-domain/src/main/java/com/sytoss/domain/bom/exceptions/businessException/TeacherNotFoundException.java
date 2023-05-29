package com.sytoss.domain.bom.exceptions.businessException;

public class TeacherNotFoundException extends NotFoundException {

    public TeacherNotFoundException (Long id) {
        super("Teacher", id);
    }
}
