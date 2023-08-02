package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService extends AbstractService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    public Exam save(Exam exam) {
        exam.setTeacher((Teacher) getCurrentUser());
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);
        examDTO = examConnector.save(examDTO);
        examConvertor.fromDTO(examDTO, exam);
        return exam;
    }

    public List<Exam> findExams() {
        AbstractUser abstractUser = getCurrentUser();

        if (abstractUser instanceof Teacher) {
            List<ExamDTO> examDTOList = examConnector.findByTeacherId(abstractUser.getId());

            return examDTOList.stream().map((examDTO) -> {
                Exam exam = new Exam();
                examConvertor.fromDTO(examDTO, exam);
                return exam;
            }).toList();
        } else {
            log.warn("User type was not valid when try to get exams by teacher id!");
            throw new UserNotIdentifiedException("User type not teacher!");
        }
    }
}
