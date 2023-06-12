package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskDomain {

    private Long id;

    private String name;

    private String script;

    private Discipline discipline;

    private List<Task> tasks;
}
