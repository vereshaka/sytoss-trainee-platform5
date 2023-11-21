package com.sytoss.lessons.services;

import com.sytoss.domain.bom.analytics.AnaliticGrade;
import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.AnalyticsConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.dto.AnalyticsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService extends AbstractService {

    private final AnalyticsConnector analyticsConnector;

    private final UserConnector userConnector;

    public List<AnalyticsDTO> checkOrCreate(long examId, long disciplineId, List<Student> students) {
        List<AnalyticsDTO> analyticsElementDTOS = new ArrayList<>();
        for (Student student : students) {
            AnalyticsDTO analyticsElementDTO = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(disciplineId, examId, student.getId());
            if (analyticsElementDTO == null) {
                analyticsElementDTO = new AnalyticsDTO();
                analyticsElementDTO.setDisciplineId(disciplineId);
                analyticsElementDTO.setExamId(examId);
                analyticsElementDTO.setStudentId(student.getId());
                analyticsElementDTO.setGrade(0.0);
                analyticsElementDTO.setTimeSpent(0L);
                analyticsElementDTO = analyticsConnector.save(analyticsElementDTO);
            }
            analyticsElementDTOS.add(analyticsElementDTO);
        }
        return analyticsElementDTOS;
    }

    public List<AnalyticsDTO> checkOrCreate(long examId, long disciplineId, long groupId) {
        List<Student> students = userConnector.getStudentOfGroup(groupId);
        return checkOrCreate(examId, disciplineId, students);
    }

    public void updateAnalyticsElement(Analytic analytic) {
        AnalyticsDTO dto = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(
                analytic.getDiscipline().getId(),
                analytic.getExam().getId(),
                analytic.getStudent().getId());
        if (dto == null) {
            log.error("Now elements found");
            //TODO: yevgenyv: correct handling
            dto = new AnalyticsDTO();
        }
        AnaliticGrade grade = analytic.getGrade();
        if (dto.getPersonalExamId() == null
                || grade.getGrade() > dto.getGrade()
                || (grade.getGrade() == dto.getGrade() && grade.getTimeSpent() < dto.getTimeSpent())) {
            dto.setPersonalExamId(analytic.getPersonalExam().getId());
            dto.setGrade(analytic.getGrade().getGrade());
            dto.setTimeSpent(analytic.getGrade().getTimeSpent());
        }
        analyticsConnector.save(dto);
    }
}
