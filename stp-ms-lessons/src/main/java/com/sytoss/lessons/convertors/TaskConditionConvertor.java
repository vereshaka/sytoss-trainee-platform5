package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.dto.TaskConditionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskConditionConvertor {

    public void fromDTO(TaskConditionDTO source, TaskCondition destination) {
        destination.setId(source.getId());
        destination.setValue(source.getName());
        destination.setType(source.getType());
    }

    public void toDTO(TaskCondition source, TaskConditionDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getValue());
        destination.setType(source.getType());
    }
}
