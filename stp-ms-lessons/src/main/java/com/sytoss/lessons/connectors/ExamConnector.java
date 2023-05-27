package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.ExamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamConnector extends JpaRepository<ExamDTO, Long> {

}
