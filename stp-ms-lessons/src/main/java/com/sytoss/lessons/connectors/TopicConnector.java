package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicConnector extends JpaRepository<TopicDTO, String> {

    List<TopicDTO> findByDisciplineId(Long disciplineId);
}
