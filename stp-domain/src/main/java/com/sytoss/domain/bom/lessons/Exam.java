package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.analytics.AnalyticFull;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Exam {

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private Long id;

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private String name;

    private Integer numberOfTasks;

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private Integer maxGrade;

    private List<Topic> topics;

    private Teacher teacher;

    private Discipline discipline;

    private List<Task> tasks;

    private List<ExamAssignee> examAssignees = new ArrayList<>();

    public Exam(Long id, String name, Integer maxGrade){
        this.id = id;
        this.name = name;
        this.maxGrade = maxGrade;
    }

}
