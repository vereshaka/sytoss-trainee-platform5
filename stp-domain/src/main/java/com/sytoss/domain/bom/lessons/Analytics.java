package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Analytics {
    private Discipline discipline;
    private Student student;
    private StudentsGrade studentsGrade;
    private List<ExamsElement> examsElementList;
}
