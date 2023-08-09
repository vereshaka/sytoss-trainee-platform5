package com.sytoss.domain.bom.exceptions.business.notfound;

public class PersonalExamNotFoundException extends NotFoundException {

    public PersonalExamNotFoundException(String id) {
        super("Personal Exam", id);
    }
}
