package com.sytoss.domain.bom.exceptions.business;

public class ExamAlreadyExists extends AlreadyExistException {

    public ExamAlreadyExists(String value) {
        super("Exam with this name is already in created!", value);
    }
}
