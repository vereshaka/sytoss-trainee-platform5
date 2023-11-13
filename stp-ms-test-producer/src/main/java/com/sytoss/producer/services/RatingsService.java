package com.sytoss.producer.services;

import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.connectors.ExamAssigneeConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RatingsService {

    private final PersonalExamConnector personalExamConnector;

    private final ExamAssigneeConnector examAssigneeConnector;

    public Double getAverageEstimation(Long examId,Long groupId){
        List<ExamAssignee> examAssignees = examAssigneeConnector.getAllExamAssignees(examId);
        List<PersonalExam> personalExams;
        for(ExamAssignee examAssignee : examAssignees){
            if()
        }

        List<PersonalExam> personalExams = personalExamConnector.getAllByStudent_PrimaryGroup_Id(groupId).stream()
                .filter(personalExam -> personalExam.getStatus().equals(PersonalExamStatus.FINISHED)
                        || personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)).toList();
        Double avg = personalExams.stream().map(PersonalExam::getSummaryGrade).reduce(0.0, Double::sum) / personalExams.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(avg).replace(",","."));
    }
}
