package com.sytoss.lessons.bdd.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamView {

    private String name;
    private String id;
    private String tasks;
    private String taskCount;
    private String maxGrade;


    public ExamView(Map<String, String> input) {
        setName(input.get("name"));
        setId(input.get("id"));
        setTasks(input.get("tasks"));
        setTaskCount(input.get("taskCount"));
        setMaxGrade(input.get("maxGrade"));
    }
}
