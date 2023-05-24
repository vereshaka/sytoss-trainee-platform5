package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupConnector extends JpaRepository<GroupDTO, String> {

    List<GroupDTO> findByDiscipline(Long disciplineId);
}
