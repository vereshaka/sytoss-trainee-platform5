package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskDomainConvertor {

    public void fromDTO(TaskDomainDTO source, TaskDomain destination) {
        if (source != null) {
            destination.setId(source.getId());
            destination.setName(source.getName());
            destination.setScript(source.getScript());
        }
    }

    public void toDTO(TaskDomain source, TaskDomainDTO destination) {
        if (source != null) {
            destination.setId(source.getId());
            destination.setName(source.getName());
            destination.setScript(source.getScript());
        }
    }
}
