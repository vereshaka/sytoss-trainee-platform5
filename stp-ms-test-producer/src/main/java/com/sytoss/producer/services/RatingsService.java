package com.sytoss.producer.services;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RatingsService {

    private final PersonalExamConnector personalExamConnector;

    public Double getAverageEstimationForGroup(Long groupId){
        List<PersonalExam> personalExams = personalExamConnector.getAllByDisciplineId(groupId).stream()
                .filter(personalExam -> personalExam.getStatus().equals(PersonalExamStatus.FINISHED)
                        || personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)).toList();
        Double avg = personalExams.stream().map(PersonalExam::getSummaryGrade).reduce(0.0, Double::sum) / personalExams.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(avg).replace(",","."));
    }

    public Double getAverageEstimationForDiscipline(Long disciplineId){
        List<PersonalExam> personalExams = personalExamConnector.getAllByDisciplineId(disciplineId).stream()
                .filter(personalExam -> personalExam.getStatus().equals(PersonalExamStatus.FINISHED)
                        || personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)).toList();
        Double avg = personalExams.stream().map(PersonalExam::getSummaryGrade).reduce(0.0, Double::sum) / personalExams.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(avg).replace(",","."));
    }
}
