package com.sytoss.domain.bom.exceptions.businessException;

public class PersonalExamAlreadyStartedException extends BusinessException {

    public PersonalExamAlreadyStartedException() {
        super("Exam is already in progress!");
    }
}
