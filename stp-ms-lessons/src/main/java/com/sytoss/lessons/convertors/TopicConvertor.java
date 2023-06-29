package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TopicConvertor {

    private final DisciplineConvertor disciplineConvertor;

    public void fromDTO(TopicDTO source, Topic destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setShortDescription(source.getShortDescription());
        destination.setFullDescription(source.getFullDescription());
        destination.setDuration(source.getDuration());
        destination.setIcon(source.getIcon());
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }

    public void toDTO(Topic source, TopicDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setShortDescription(source.getShortDescription());
        destination.setFullDescription(source.getFullDescription());
        destination.setDuration(source.getDuration());
        destination.setIcon(source.getIcon());
        DisciplineDTO discipline = new DisciplineDTO();
        disciplineConvertor.toDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }
}
