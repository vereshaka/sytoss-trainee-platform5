package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineConnector extends JpaRepository<DisciplineDTO, Long> {

    DisciplineDTO getByNameAndTeacherId(String disciplineName, Long teacherId);

    List<DisciplineDTO> findByTeacherId(Long teacherId);
}
