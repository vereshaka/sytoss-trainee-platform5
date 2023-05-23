package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupConvertor {

    @Autowired
    private DisciplineConvertor disciplineConvertor;

    public void fromDTO(GroupDTO source, Group destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }

    public void toDTO(Group source, GroupDTO destination) {
        if (source.getId() != null) {
            destination.setId(source.getId());
        }
        destination.setName(source.getName());
        DisciplineDTO discipline = new DisciplineDTO();
        disciplineConvertor.toDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
    }
}
