package com.sytoss.lessons.services;

import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupService extends AbstractService {

    private final GroupReferenceConnector groupReferenceConnector;

    public List<GroupReferenceDTO> findGroups() {
       return groupReferenceConnector.findByDisciplineId_TeacherId(getCurrentUser().getId());
    }
}
