package com.sytoss.lessons.bom;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamModelForStudents {

    private Exam exam;

    private List<Student> students;
}
