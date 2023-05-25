package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicConvertor {

    @Autowired
    private DisciplineConvertor disciplineConvertor;

    public void fromDTO(TopicDTO source, Topic destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }

    public void toDTO(Topic source, TopicDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        DisciplineDTO discipline = new DisciplineDTO();
        disciplineConvertor.toDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }
}
