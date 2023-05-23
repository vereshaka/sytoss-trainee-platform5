package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicConnector extends JpaRepository<TopicDTO, String> {

    List<TopicDTO> findByDisciplineId(String disciplineId);
}
