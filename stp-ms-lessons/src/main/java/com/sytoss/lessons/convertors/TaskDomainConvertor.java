package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.dto.TaskDomainDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskDomainConvertor {

    public void fromDTO(TaskDomainDTO source, TaskDomain destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setScript(source.getScript());
    }

    public void toDTO(TaskDomain source, TaskDomainDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setScript(source.getScript());
    }
}
