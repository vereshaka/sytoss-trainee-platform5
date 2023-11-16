package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsConvertor {

    public void fromDTO(AnalyticsElementDTO source, AnalyticsElement destination) {
        destination.setDisciplineId(source.getDisciplineId());
        destination.setExamId(source.getExamId());
        destination.setStudentId(source.getStudentId());
        destination.setPersonalExamId(source.getPersonalExamId());
        destination.setGrade(source.getGrade());
        destination.setTimeSpent(source.getTimeSpent());
    }

    public void toDTO(AnalyticsElement source, AnalyticsElementDTO destination) {
        if (source != null) {
            destination.setDisciplineId(source.getDisciplineId());
            destination.setExamId(source.getExamId());
            destination.setStudentId(source.getStudentId());
            destination.setPersonalExamId(source.getPersonalExamId());
            destination.setGrade(source.getGrade());
            destination.setTimeSpent(source.getTimeSpent());
        }
    }
}
