package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonalExamByStudentsModel {
    private Long disciplineId;
    private List<Long> examAssignees;
    private List<Student> students;
}
