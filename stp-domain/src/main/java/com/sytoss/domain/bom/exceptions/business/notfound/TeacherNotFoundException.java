package com.sytoss.domain.bom.exceptions.business.notfound;

public class TeacherNotFoundException extends NotFoundException {

    public TeacherNotFoundException (Long id) {
        super("Teacher", id);
    }
}
