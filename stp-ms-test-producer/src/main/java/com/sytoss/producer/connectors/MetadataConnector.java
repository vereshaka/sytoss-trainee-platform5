package com.sytoss.producer.connectors;

import com.sytoss.producer.bom.Discipline;
import com.sytoss.producer.bom.Task;
import com.sytoss.producer.bom.TaskDomain;
import com.sytoss.producer.bom.Topic;

import java.util.List;

public interface MetadataConnector {

    Discipline getDiscipline(Long id);

    Topic getTopic(Long id);

    List<Task> getTasksForTopic(Long id);

    TaskDomain getTaskDomain(Long id);
}
