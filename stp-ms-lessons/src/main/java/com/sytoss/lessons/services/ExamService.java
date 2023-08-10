package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import jakarta.persistence.EntityNotFoundException;
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

    private final UserConnector userConnector;

    private final PersonalExamConnector personalExamConnector;

    public Exam save(Exam exam) {
        exam.setTeacher((Teacher) getCurrentUser());
        //TODO: yevgenyv: re think it
        exam.setDiscipline(exam.getTopics().get(0).getDiscipline());
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);
        examDTO = examConnector.save(examDTO);
        examConvertor.fromDTO(examDTO, exam);
        List<Student> students = userConnector.getStudentOfGroup(exam.getGroup().getId());
        for (Student student: students){
            try {
                personalExamConnector.create(new ExamConfiguration(exam, student));
            } catch (Exception e) {
                //TODO: yevgenyv: need to re think return answer
                log.error("Could not create a personal exam for student", e);
            }
        }
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

    public Exam getById(Long examId) {
        try {
            ExamDTO taskDTO = examConnector.getReferenceById(examId);
            Exam exam = new Exam();
            examConvertor.fromDTO(taskDTO, exam);
            return exam;
        } catch (EntityNotFoundException e) {
            throw new ExamNotFoundException(examId);
        }
    }
}
