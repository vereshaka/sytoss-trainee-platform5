package com.sytoss.lessons.bdd.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineConnector extends JpaRepository<DisciplineDTO, String> {

    DisciplineDTO getByName(String name);
}
