package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeToDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAssigneeToConnector extends JpaRepository<ExamAssigneeToDTO, Long> {
    List<ExamAssigneeToDTO> getAllByParent_Id(Long examAssigneeDTOId);
}
