package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.stereotype.Component;

@Component
public class DisciplineConvertor {

    public void fromDTO(DisciplineDTO source, Discipline destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    public void toDTO(Discipline source, DisciplineDTO destination) {
        if (source.getId() != null) {
            destination.setId(source.getId());
        }
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}