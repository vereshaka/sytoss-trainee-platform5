package com.sytoss.producer.connectors;

import com.sytoss.producer.bom.PersonalExam;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PersonalExamConnector extends MongoRepository<PersonalExam, String> {

}
