package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskDomainConvertor {

    private final DisciplineConvertor disciplineConvertor;

    public void fromDTO(TaskDomainDTO source, TaskDomain destination) {
        destination.setId(source.getId());
        if (source.getId() != null) {
            destination.setId(source.getId());
        }
        destination.setName(source.getName());
        destination.setScript(source.getScript());
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }

    public void toDTO(TaskDomain source, TaskDomainDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setScript(source.getScript());
        DisciplineDTO discipline = new DisciplineDTO();
        disciplineConvertor.toDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }
}