package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineConnector extends JpaRepository<DisciplineDTO, Long> {

    DisciplineDTO getByName(String name);
}
