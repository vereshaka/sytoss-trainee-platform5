package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    public Exam save(Exam exam) {
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);
        examDTO = examConnector.save(examDTO);
        examConvertor.fromDTO(examDTO, exam);
        return exam;
    }
}
