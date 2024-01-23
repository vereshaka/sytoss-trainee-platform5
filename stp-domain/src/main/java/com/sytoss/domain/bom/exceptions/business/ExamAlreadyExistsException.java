package com.sytoss.domain.bom.exceptions.business;

public class ExamAlreadyExistsException extends AlreadyExistException {

    public ExamAlreadyExistsException(String value) {
        super("Exam with this name is already in created!", value);
    }
}
