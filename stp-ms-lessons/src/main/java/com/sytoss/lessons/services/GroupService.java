package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService extends AbstractService{

    private final GroupConnector groupConnector;

    private final GroupConvertor groupConvertor;

    private final DisciplineService disciplineService;

    @PersistenceContext
    private EntityManager entityManager;

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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GroupDTO> criteriaQuery = criteriaBuilder.createQuery(GroupDTO.class);
        Root<GroupDTO> dtoRoot = criteriaQuery.from(GroupDTO.class);
        Join<GroupDTO,DisciplineDTO> join = dtoRoot.join("discipline", JoinType.INNER);
        criteriaQuery.where(criteriaBuilder.equal(join.get("teacherId"),getCurrentUser().getId()));
        List<GroupDTO> groupDTOS = entityManager.createQuery(criteriaQuery).getResultList();

        List<Group> result = new ArrayList<>();
        for (GroupDTO groupDTO : groupDTOS) {
            Group group = new Group();
            groupConvertor.fromDTO(groupDTO, group);
            result.add(group);
        }
        return result;
    }
}
