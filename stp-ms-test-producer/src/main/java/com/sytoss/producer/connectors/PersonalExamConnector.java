package com.sytoss.producer.connectors;

import com.sytoss.producer.bom.PersonalExam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalExamConnector extends MongoRepository<PersonalExam, String> {

}
