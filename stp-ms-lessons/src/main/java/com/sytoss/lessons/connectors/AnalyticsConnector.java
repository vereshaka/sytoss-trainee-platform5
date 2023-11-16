package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.AnalyticsElementDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsConnector extends JpaRepository<AnalyticsElementDTO, Long> {

    AnalyticsElementDTO getByDisciplineIdAndExamIdAndStudentId(Long disciplineId, Long examId, Long studentId);

    List<AnalyticsElementDTO> deleteAnalyticsElementDTOByDisciplineIdAndStudentId(Long disciplineId, Long studentId);

    List<AnalyticsElementDTO> deleteAllByExamId(Long examId);
}
