package com.sytoss.domain.bom.exceptions.business;

public class PersonalExamAlreadyStartedException extends BusinessException {

    public PersonalExamAlreadyStartedException() {
        super("Exam is already in progress!");
    }
}
