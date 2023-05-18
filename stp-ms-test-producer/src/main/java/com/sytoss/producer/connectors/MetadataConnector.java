package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.Discipline;
import com.sytoss.domain.bom.Task;
import com.sytoss.domain.bom.TaskDomain;
import com.sytoss.domain.bom.Topic;

import java.util.List;

public interface MetadataConnector {

    Discipline getDiscipline(Long id);

    Topic getTopic(Long id);

    List<Task> getTasksForTopic(Long id);

    TaskDomain getTaskDomain(Long id);
}
