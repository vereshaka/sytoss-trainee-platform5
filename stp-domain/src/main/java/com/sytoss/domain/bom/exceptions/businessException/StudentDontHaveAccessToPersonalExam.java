package com.sytoss.domain.bom.exceptions.businessException;

public class StudentDontHaveAccessToPersonalExam extends BusinessException{

    public StudentDontHaveAccessToPersonalExam(String studentId, Long personalExamId) {
        super("Student with id: " + studentId + " dont has access to personal exam with id: " + personalExamId);
    }
}
