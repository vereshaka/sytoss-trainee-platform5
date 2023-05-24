package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineConnector extends JpaRepository<DisciplineDTO, String> {
}
