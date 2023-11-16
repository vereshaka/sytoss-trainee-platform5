package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.RatingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingConnector extends JpaRepository<RatingDTO, Long> {

    RatingDTO getByDisciplineIdAndExamIdAndStudentId(Long disciplineId, Long examId, Long studentId);

    List<RatingDTO> deleteRatingDTOByDisciplineIdAndStudentId(Long disciplineId, Long studentId);
}
