package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAssigneeConnector extends JpaRepository<ExamAssigneeDTO, Long> {
    List<ExamAssigneeDTO> getAllByExam_Id(Long examId);
}
