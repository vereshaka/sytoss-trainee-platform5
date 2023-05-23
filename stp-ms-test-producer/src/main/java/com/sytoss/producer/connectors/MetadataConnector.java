package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;

import java.util.List;

public interface MetadataConnector {

    Discipline getDiscipline(String id);

    Topic getTopic(String id);

    List<Task> getTasksForTopic(String id);

    TaskDomain getTaskDomain(Long id);
}
