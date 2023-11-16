package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.lessons.dto.RatingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingConvertor {

    public void fromDTO(RatingDTO source, Rating destination) {
        destination.setDisciplineId(source.getDisciplineId());
        destination.setExamId(source.getExamId());
        destination.setStudentId(source.getStudentId());
        destination.setPersonalExamId(source.getPersonalExamId());
        destination.setGrade(source.getGrade());
        destination.setTimeSpent(source.getTimeSpent());
    }

    public void toDTO(Rating source, RatingDTO destination) {
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
