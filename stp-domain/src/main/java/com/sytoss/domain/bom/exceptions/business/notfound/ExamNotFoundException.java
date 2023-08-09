package com.sytoss.domain.bom.exceptions.business.notfound;

public class ExamNotFoundException extends NotFoundException {
    public ExamNotFoundException(Long id) {
        super("exam", id);
    }
}
