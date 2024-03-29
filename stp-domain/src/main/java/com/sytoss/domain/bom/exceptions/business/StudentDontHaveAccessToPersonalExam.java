package com.sytoss.domain.bom.exceptions.business;

public class StudentDontHaveAccessToPersonalExam extends BusinessException {

    public StudentDontHaveAccessToPersonalExam(Long studentId, String personalExamId) {
        super("Student with id: " + studentId + " dont has access to personal exam with id: " + personalExamId);
    }
}
