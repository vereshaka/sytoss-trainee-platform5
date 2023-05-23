package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicConnector extends JpaRepository<Topic, String> {

    List<Topic> findByDisciplineId(String disciplineId);
}
