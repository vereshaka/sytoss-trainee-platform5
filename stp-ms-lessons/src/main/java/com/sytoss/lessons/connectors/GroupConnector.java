package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.users.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupConnector extends MongoRepository<Group, String> {

    List<Group> findByDiscipline_Id(Long disciplineId);
}
