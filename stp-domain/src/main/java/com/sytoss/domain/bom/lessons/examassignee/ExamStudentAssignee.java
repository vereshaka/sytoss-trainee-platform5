package com.sytoss.domain.bom.lessons.examassignee;

import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExamStudentAssignee extends ExamAssignee {
    List<Student> students = new ArrayList<>();
}
