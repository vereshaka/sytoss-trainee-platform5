package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.AnalyticsAverageDTO;
import com.sytoss.lessons.dto.AnalyticsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsConnector extends CrudRepository<AnalyticsDTO, Long> {

    AnalyticsDTO getByDisciplineIdAndExamIdAndStudentId(Long disciplineId, Long examId, Long studentId);

    List<AnalyticsDTO> getByDisciplineIdAndStudentId(Long disciplineId, Long studentId);

    List<AnalyticsDTO> deleteAnalyticsDTOByDisciplineIdAndStudentId(Long disciplineId, Long studentId);

    List<AnalyticsDTO> deleteAllByExamId(Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId and a.studentId in :studentIds group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndGroupId(@Param("disciplineId") Long disciplineId, @Param("studentIds") List<Long> studentsIds);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId and a.examId = :examId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndExamId(@Param("disciplineId") Long disciplineId, @Param("examId") Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId and a.studentId in :studentIds and a.examId = :examId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndGroupIdAndExamId(Long disciplineId, @Param("studentIds") List<Long> studentsIds, Long examId);

}
