package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicConnector extends MongoRepository<Topic, String> {

    List<Topic> findByDisciplineId(String disciplineId);
}
