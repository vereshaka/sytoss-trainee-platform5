package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TaskDomainDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDomainConnector extends JpaRepository<TaskDomainDTO, Long> {

    TaskDomainDTO getByNameAndDisciplineId(String name, Long disciplineId);

    List<TaskDomainDTO> findByDisciplineId(Long disciplineId);
}
