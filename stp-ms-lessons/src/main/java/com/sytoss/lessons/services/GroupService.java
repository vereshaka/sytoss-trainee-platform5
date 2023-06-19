package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService {

    private final GroupConnector groupConnector;

    private final GroupConvertor groupConvertor;

    private final DisciplineService disciplineService;

    public List<Group> findByDiscipline(Long disciplineId) {
        List<GroupDTO> groupDTOList = groupConnector.findByDisciplineId(disciplineId);
        List<Group> result = new ArrayList<>();
        for (GroupDTO groupDTO : groupDTOList) {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            result.add(group);
        }
        return result;
    }

    public Group create(Long disciplineId, Group group) {
        GroupDTO groupDTO = groupConnector.getByNameAndDisciplineId(group.getName(), disciplineId);
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            Discipline discipline = disciplineService.getById(disciplineId);
            group.setDiscipline(discipline);
            groupConvertor.toDTO(group, groupDTO);
            groupDTO = groupConnector.save(groupDTO);
            groupConvertor.fromDTO(groupDTO, group);
            return group;
        }
        throw new GroupExistException(group.getName());
    }

    public List<Group> findGroups() {
        List<Discipline> disciplineDTOList = disciplineService.findDisciplines();

        List<GroupDTO> allGroups = new ArrayList<>();
        for(Discipline discipline : disciplineDTOList){
            List<GroupDTO> groupDTOS = groupConnector.findByDisciplineId(discipline.getId());
            for(GroupDTO groupDTO : groupDTOS){
                if(!allGroups.contains(groupDTO.getId())){
                    allGroups.add(groupDTO);
                }
            }
        }

        List<Group> result = new ArrayList<>();
        for (GroupDTO groupDTO : allGroups) {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            result.add(group);
        }
        return result;
    }
}
