package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TaskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskConnector extends JpaRepository<TaskDTO, Long> {

    TaskDTO getById(long taskId);

    TaskDTO getByQuestionAndTopicsDisciplineId(String question, Long disciplineId);
}
