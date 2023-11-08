package com.sytoss.domain.bom.lessons.examassignee;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamAssignee {
    private Long id;

    private Date relevantFrom;

    private Date relevantTo;

    private Integer duration;

    private List<Student> students = new ArrayList<>();

    private List<Group> groups = new ArrayList<>();

    private Exam exam = new Exam();
}
