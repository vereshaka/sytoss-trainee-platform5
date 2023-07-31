package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.ExamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamConnector extends JpaRepository<ExamDTO, Long> {

    ExamDTO getByNameAndGroupId(String examName, Long groupId);

    List<ExamDTO> findByTopicsId(Long topicId);

    List<ExamDTO> findByTeacherId(Long teacherId);
}
