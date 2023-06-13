package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCondition {

    private Long id;

    private String value;

    private ConditionType type;

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if(!(object instanceof TaskCondition)) return false;
        TaskCondition taskCondition = (TaskCondition) object;
        return taskCondition.getId() == id && (value == taskCondition.getValue() || value != null && value.equals(taskCondition.getValue())) && type == taskCondition.getType();
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + value.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
