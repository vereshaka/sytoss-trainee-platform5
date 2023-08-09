package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PersonalExamConnector extends MongoRepository<PersonalExam, String> {

    PersonalExam getById(String id);

    int countByAnswersTaskTaskDomainIdAndStatusNotLike(Long id, PersonalExamStatus status);

    List<PersonalExam> getAllByExamId(Long examId);

    List<PersonalExam> getAllByStudent_Id(Long studentId);
}
