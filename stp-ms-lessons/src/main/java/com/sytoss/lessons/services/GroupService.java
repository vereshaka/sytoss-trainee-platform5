package com.sytoss.lessons.services;

import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService extends AbstractService{

    private final DisciplineConnector disciplineConnector;

    public List<GroupReferenceDTO> findGroups() {
        List<DisciplineDTO> disciplineDTOS = disciplineConnector.findByTeacherId(getCurrentUser().getId());
        List<GroupReferenceDTO> allGroups = new ArrayList<>();
        for(DisciplineDTO disciplineDTO : disciplineDTOS){
            allGroups.addAll(disciplineDTO.getGroupReferences());
        }

       return allGroups.stream().distinct().toList();
    }
}
