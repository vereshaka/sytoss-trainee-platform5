package com.sytoss.domain.bom.exceptions.business.notfound;

public class StudentNotFoundException extends NotFoundException {

    public StudentNotFoundException (String email) {
        super("Student", email);
    }
}
