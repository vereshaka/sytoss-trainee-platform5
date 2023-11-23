package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.lessons.Exam;
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

    List<AnalyticsDTO> deleteAllByDisciplineId(Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent), row_number() over (order by AVG(a.grade), AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);
    @Query("SELECT new com.sytoss.domain.bom.analytics.AnaliticGrade(avg(a.grade), cast(avg(a.timeSpent) as long)) " +
            "from ANALYTICS a " +
            "where a.disciplineId = :disciplineId " +
            "and a.studentId = :studentId " +
            "and a.personalExamId is not null ")
    AnalyticGrade getAverageGrade(Long disciplineId, Long studentId);

    @Query("SELECT new com.sytoss.domain.bom.analytics.AnaliticGrade(max(a.grade), max(a.timeSpent)) " +
            "from ANALYTICS a " +
            "where a.disciplineId = :disciplineId " +
            "and a.studentId = :studentId " +
            "and a.personalExamId is not null ")
    AnalyticGrade getMaxGrade(Long disciplineId, Long studentId);

    @Query("SELECT new com.sytoss.domain.bom.lessons.Exam(e.id, e.name, cast(max(a.grade) as int)) " +
            "from ANALYTICS a, EXAM e " +
            "where e.id = :examId " +
            "and a.examId = e.id " +
            "and a.personalExamId is not null " +
            "group by e.id, e.name")
    Exam getExamInfo(Long examId);

/*
    @Query("SELECT new com.sytoss.domain.bom.lessons.analytics.RatingModel(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId group by a.studentId order by 2, 3")
    List<RatingModel> getStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade), AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.studentId in :studentIds group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndGroupId(@Param("disciplineId") Long disciplineId, @Param("studentIds") List<Long> studentsIds);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade), AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.examId = :examId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndExamId(@Param("disciplineId") Long disciplineId, @Param("examId") Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade), AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.studentId in :studentIds and a.examId = :examId group by a.studentId order by 2, 3")
    List<AnalyticsAverageDTO> getStudentRatingsByDisciplineAndGroupIdAndExamId(Long disciplineId, @Param("studentIds") List<Long> studentsIds, Long examId);

}
