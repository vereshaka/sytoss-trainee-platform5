package com.sytoss.lessons.bdd.connectors;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicTestConnector extends TopicConnector {

    TopicDTO getByName(String name);
}
