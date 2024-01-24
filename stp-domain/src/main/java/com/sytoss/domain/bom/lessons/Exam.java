package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Exam {

    private Long id;

    private String name;

    private Integer numberOfTasks;

    private Integer maxGrade;

    private List<Topic> topics;

    private Teacher teacher;

    private Discipline discipline;

    private List<Task> tasks;

    private List<ExamAssignee> examAssignees = new ArrayList<>();

    private Date creationDate = new Date();

    private Date lastModifiedDate;
}
