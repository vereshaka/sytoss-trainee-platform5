package com.sytoss.producer.services;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService extends AbstractService {

    @Autowired
    private PersonalExamService personalExamService;

    public List<PersonalExam> getPersonalExams(Long examId) {
        return personalExamService.getAllByExamId(examId);
    }
}
