package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TaskDomainDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDomainConnector extends JpaRepository<TaskDomainDTO, Long> {

}
