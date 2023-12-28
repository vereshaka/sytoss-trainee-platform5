package com.sytoss.lessons.connectors;

import com.sytoss.lessons.controllers.views.ExamSummaryStatistic;
import com.sytoss.lessons.dto.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AnalyticsConnector extends CrudRepository<AnalyticsDTO, Long> {

    AnalyticsDTO getByDisciplineIdAndExamIdAndStudentId(Long disciplineId, Long examId, Long studentId);

    List<AnalyticsDTO> getByDisciplineIdAndStudentIdAndPersonalExamIdIsNotNull(Long disciplineId, Long studentId);

    List<AnalyticsDTO> deleteAnalyticsDTOByDisciplineIdAndStudentId(Long disciplineId, Long studentId);

    List<AnalyticsDTO> deleteAllByExamId(Long examId);

    List<AnalyticsDTO> deleteAllByDisciplineId(Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent), row_number() over (order by AVG(a.grade) desc, AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsAverageDTO> getAvgStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade) desc, AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.studentId in :studentIds group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsAverageDTO> getAvgStudentRatingsByDisciplineAndGroupId(@Param("disciplineId") Long disciplineId, @Param("studentIds") List<Long> studentsIds);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade) desc , AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.examId = :examId group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsAverageDTO> getAvgStudentRatingsByDisciplineAndExamId(@Param("disciplineId") Long disciplineId, @Param("examId") Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsAverageDTO(a.studentId, AVG(a.grade), AVG(a.timeSpent),  row_number() over (order by AVG(a.grade) desc , AVG(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.studentId in :studentIds and a.examId = :examId group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsAverageDTO> getAvgStudentRatingsByDisciplineAndGroupIdAndExamId(Long disciplineId, @Param("studentIds") List<Long> studentsIds, Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.SummaryGradeDTO(" +
            "COALESCE(max(a.grade),0), " +
            "COALESCE(min(a.timeSpent),0), " +
            "COALESCE(avg(a.grade),0), " +
            "cast(COALESCE(avg(a.timeSpent),0) as Long)) " +
            "from ANALYTICS a " +
            "where a.disciplineId = :disciplineId " +
            "and a.studentId = :studentId " +
            "and a.personalExamId is not null ")
    SummaryGradeDTO getSummaryGrade(Long disciplineId, Long studentId);

    @Query("SELECT new com.sytoss.lessons.controllers.views.ExamSummaryStatistic(e.id, e.name, e.maxGrade, cast(max(a.grade) as Integer)) " +
            "from ANALYTICS a, EXAM e " +
            "where e.id = :examId " +
            "and a.examId = e.id " +
            "and a.personalExamId is not null " +
            "group by e.id, e.name")
    ExamSummaryStatistic getExamInfo(Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.SummaryGradeDTO(" +
            "COALESCE(max(a.grade),0), " +
            "COALESCE(min(a.timeSpent),0), " +
            "COALESCE(avg(a.grade),0), " +
            "cast(COALESCE(avg(a.timeSpent),0) as Long)" +
            ") " +
            "from ANALYTICS a " +
            "where a.disciplineId = :disciplineId " +
            "and a.studentId in (:studentIds) " +
            "and a.personalExamId is not null ")
    SummaryGradeDTO getStudentsGradeByDiscipline(Long disciplineId, Set<Long> studentIds);

    @Query("SELECT new com.sytoss.lessons.dto.SummaryGradeByExamDTO(" +
            "COALESCE(max(a.grade),0), " +
            "COALESCE(min(a.timeSpent),0), " +
            "COALESCE(avg(a.grade),0), " +
            "cast(COALESCE(avg(a.timeSpent), 0) as Long), e.id, e.maxGrade, e.name, e.creationDate) " +
            "from ANALYTICS a, EXAM e " +
            "where e.id = a.examId " +
            "and a.disciplineId = :disciplineId " +
            "and a.studentId in (:studentIds) " +
            "and a.personalExamId is not null " +
            "group by e.id, e.maxGrade, e.name, e.creationDate")
    List<SummaryGradeByExamDTO> getStudentsGradeByExam(Long disciplineId, Set<Long> studentIds);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsSummaryDTO(a.studentId, SUM(a.grade), SUM(a.timeSpent), row_number() over (order by SUM(a.grade) desc, SUM(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsSummaryDTO> getSumStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsSummaryDTO(a.studentId, SUM(a.grade), SUM(a.timeSpent),  row_number() over (order by SUM(a.grade) desc, SUM(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.studentId in :studentIds group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsSummaryDTO> getSumStudentRatingsByDisciplineAndGroupId(@Param("disciplineId") Long disciplineId, @Param("studentIds") List<Long> studentsIds);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsSummaryDTO(a.studentId, SUM(a.grade), SUM(a.timeSpent),  row_number() over (order by SUM(a.grade) desc , SUM(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.examId = :examId group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsSummaryDTO> getSumStudentRatingsByDisciplineAndExamId(@Param("disciplineId") Long disciplineId, @Param("examId") Long examId);

    @Query("SELECT new com.sytoss.lessons.dto.AnalyticsSummaryDTO(a.studentId, SUM(a.grade), SUM(a.timeSpent),  row_number() over (order by SUM(a.grade) desc , SUM(a.timeSpent))) from ANALYTICS a where a.disciplineId = :disciplineId and a.personalExamId is not null and a.studentId in :studentIds and a.examId = :examId group by a.studentId order by 2 desc, 3 asc")
    List<AnalyticsSummaryDTO> getSumStudentRatingsByDisciplineAndGroupIdAndExamId(Long disciplineId, @Param("studentIds") List<Long> studentsIds, Long examId);

    List<AnalyticsDTO> deleteAllByDisciplineIdAndStudentId(Long disciplineId,Long studentId);
}
