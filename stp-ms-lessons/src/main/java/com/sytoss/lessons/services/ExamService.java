package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    @Autowired
    private ExamConnector examConnector;

    @Autowired
    private ExamConvertor examConvertor;

    public void save(Exam exam) {
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);
        examConnector.save(examDTO);
    }
}
