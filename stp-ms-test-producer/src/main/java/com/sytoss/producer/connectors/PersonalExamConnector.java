package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonalExamConnector extends MongoRepository<PersonalExam, String> {

    PersonalExam getById(String id);

    int countByAnswersTaskTaskDomainIdAndStatusNotLike(Long id, PersonalExamStatus status);

    List<PersonalExam> getAllByExamId(Long examId);

    List<PersonalExam> getAllByStudent_IdOrderByAssignedDateDesc(Long studentId);

    List<PersonalExam> getAllByTeacher_IdOrderByAssignedDateDesc(Long teacherId);
}
