package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicConnector extends JpaRepository<TopicDTO, Long> {

    List<TopicDTO> findByDisciplineIdOrderByName(Long disciplineId);

    @Query("SELECT SUM(t.duration) FROM TOPIC t WHERE t.discipline.id = :#{#disciplineId}")
    Double countDurationByDisciplineId(@Param("disciplineId")Long disciplineId);

    TopicDTO getByNameAndDisciplineId(String name, Long disciplineId);
}
