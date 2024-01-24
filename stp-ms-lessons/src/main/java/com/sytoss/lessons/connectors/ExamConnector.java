package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamConnector extends JpaRepository<ExamDTO, Long> {

    ExamDTO getByName(String examName);

    List<ExamDTO> findByTopicsId(Long topicId);

    List<ExamDTO> findByTeacherIdOrderByCreationDateDesc(Long teacherId);

    List<ExamDTO> findByTasks_Id(Long taskId);

    List<ExamDTO> findByTopics_Discipline_Id(Long disciplineId);

    List<ExamDTO> findByDiscipline_Id(Long disciplineId);

    List<ExamDTO> findByTasks_TaskDomain_Id(Long taskDomainId);

    ExamDTO findByExamAssignees_Id(Long examAssigneeId);

    List<ExamDTO> getAllByTopicsContaining(TopicDTO topic);

    ExamDTO getByNameAndDiscipline_Id(String name, Long disciplineId);

    ExamDTO getByNameAndDiscipline_IdAndIdIsNot(String name, Long disciplineId, Long id);
}
