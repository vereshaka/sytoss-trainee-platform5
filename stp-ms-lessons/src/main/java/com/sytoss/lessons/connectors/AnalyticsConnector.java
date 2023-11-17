package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.analytics.RatingModel;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsConnector extends CrudRepository<AnalyticsElementDTO, Long> {

    AnalyticsElementDTO getByDisciplineIdAndExamIdAndStudentId(Long disciplineId, Long examId, Long studentId);

    List<AnalyticsElementDTO> deleteAnalyticsElementDTOByDisciplineIdAndStudentId(Long disciplineId, Long studentId);

    List<AnalyticsElementDTO> deleteAllByExamId(Long examId);

    @Query("SELECT new com.sytoss.domain.bom.lessons.analytics.RatingModel(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId group by a.studentId order by 2, 3")
    List<RatingModel> getStudentRatingsByDiscipline(@Param("disciplineId") Long disciplineId);

    @Query("SELECT new com.sytoss.domain.bom.lessons.analytics.RatingModel(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a inner join group2discipline g on a.disciplineId = g.discipline.id where a.disciplineId = :disciplineId and g.groupId = :groupId  group by a.studentId order by 2, 3")
    List<RatingModel> getStudentRatingsByDisciplineAndGroupId(@Param("disciplineId") Long disciplineId, @Param("groupId") Long groupId);

    @Query("SELECT new com.sytoss.domain.bom.lessons.analytics.RatingModel(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a where a.disciplineId = :disciplineId and a.examId = :examId group by a.studentId order by 2, 3")
    List<RatingModel> getStudentRatingsByDisciplineAndExamId(@Param("disciplineId") Long disciplineId, @Param("examId") Long examId);

    @Query("SELECT new com.sytoss.domain.bom.lessons.analytics.RatingModel(a.studentId, AVG(a.grade), AVG(a.timeSpent)) from ANALYTICS a inner join group2discipline g on a.disciplineId = g.discipline.id where a.disciplineId = :disciplineId and g.groupId = :groupId and a.examId = :examId group by a.studentId order by 2, 3")
    List<RatingModel> getStudentRatingsByDisciplineAndGroupIdAndExamId(Long disciplineId, Long groupId, Long examId);

}
