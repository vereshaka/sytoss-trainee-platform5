package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
        return Objects.equals(taskCondition.getId(), id) && Objects.equals(value, taskCondition.getValue()) && type == taskCondition.getType();
    }

    public String getCondition(){
        StringBuilder conditionBuilder = new StringBuilder();
        if(type.equals(ConditionType.NOT_CONTAINS)){
            conditionBuilder.append("!");
        }
        return conditionBuilder.append(value).toString();
    }
}