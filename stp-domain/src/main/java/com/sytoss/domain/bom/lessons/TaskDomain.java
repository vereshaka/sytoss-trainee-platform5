package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDomain {

    private Long id;

    private String name;

    private String script;

    private Discipline discipline;

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if(!(object instanceof TaskDomain)) return false;
        TaskDomain taskDomain = (TaskDomain) object;
        return taskDomain.getId() == id && (name == taskDomain.getName() || name != null && name.equals(taskDomain.getName()))
                && (script == taskDomain.getScript() || script != null && script.equals(taskDomain.getScript()))
                && (discipline != null &&discipline.equals(taskDomain.getDiscipline()));
    }
}
