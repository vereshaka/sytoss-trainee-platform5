package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TaskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskConnector extends JpaRepository<TaskDTO, Long> {

    TaskDTO getById(long taskId);

    TaskDTO getByQuestionAndTopicsDisciplineId(String question, Long disciplineId);

    List<TaskDTO> findByTopicsId(Long id);

    List<TaskDTO> findByTaskDomainId(Long taskDomainId);

    TaskDTO getByQuestionAndTaskDomainId(String question, Long id);

    List<TaskDTO> getByTaskDomainDisciplineId(Long disciplineId);
}
