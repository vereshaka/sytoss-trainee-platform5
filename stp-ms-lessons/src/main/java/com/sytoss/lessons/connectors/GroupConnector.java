package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupConnector extends MongoRepository<GroupDTO, String> {

    List<GroupDTO> findByDiscipline(Long disciplineId);
}
