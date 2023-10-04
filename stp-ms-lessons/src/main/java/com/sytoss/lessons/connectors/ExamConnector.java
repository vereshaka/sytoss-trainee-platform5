package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamConnector extends JpaRepository<ExamDTO, Long> {

    ExamDTO getByNameAndGroupId(String examName, Long groupId);

    List<ExamDTO> findByTopicsId(Long topicId);

    List<ExamDTO> findByTeacherId(Long teacherId);

    List<ExamDTO> findByTasks_Id(Long taskId);

    List<ExamDTO> findByTopics_Discipline_Id(Long disciplineId);

    List<ExamDTO> findByTasks_TaskDomain_Id(Long taskDomainId);
}
