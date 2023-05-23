package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.connectors.GroupConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupConnector groupConnector;

    public List<Group> findByDisciplineId(Long disciplineId){
        return groupConnector.findByDiscipline_Id(disciplineId);
    }
}
