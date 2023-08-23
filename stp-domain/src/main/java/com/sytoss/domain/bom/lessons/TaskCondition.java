package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCondition {

    private Long id;

    @JsonView({PersonalExam.TeacherOnly.class})
    private String value;

    private ConditionType type;

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof TaskCondition)) return false;
        TaskCondition taskCondition = (TaskCondition) object;
        return taskCondition.getId() == id && (value == taskCondition.getValue()
                || value != null && value.equals(taskCondition.getValue()))
                && type == taskCondition.getType();
    }
}