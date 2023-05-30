package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupConnector extends JpaRepository<GroupDTO, Long> {

    List<GroupDTO> findByDisciplineId(Long disciplineId);

    GroupDTO getByNameAndDisciplineId(String groupName, Long disciplineId);
}
