package com.sytoss.domain.bom.exceptions.business.notfound;

public class ExamNotFoundException extends NotFoundException {
    public ExamNotFoundException(Long id) {
        super("exam", id);
    }

    public ExamNotFoundException(String name) {
        super("exam", name);
    }
}
