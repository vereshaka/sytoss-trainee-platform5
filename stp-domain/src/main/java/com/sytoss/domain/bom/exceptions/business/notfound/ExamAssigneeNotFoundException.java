package com.sytoss.domain.bom.exceptions.business.notfound;

public class ExamAssigneeNotFoundException extends NotFoundException {

    public ExamAssigneeNotFoundException(Long id) {
        super("Exam assignee", id);
    }
}
