package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalExamConnector extends MongoRepository<PersonalExam, String> {

    PersonalExam getById(String id);
}
