package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TaskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskConnector extends JpaRepository<TaskDTO, Long> {

    TaskDTO getByIdAndDeleteDateIsNull(long taskId);

    TaskDTO getByQuestionAndTopicsDisciplineIdAndDeleteDateIsNull(String question, Long disciplineId);

    List<TaskDTO> findByTopicsIdAndDeleteDateIsNull(Long id);

    List<TaskDTO> findByTaskDomainIdAndDeleteDateIsNull(Long taskDomainId);

    TaskDTO getByQuestionAndTaskDomainIdAndDeleteDateIsNull(String question, Long id);

    List<TaskDTO> getByTaskDomainDisciplineIdAndDeleteDateIsNull(Long disciplineId);
}
