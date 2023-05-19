package com.sytoss.producer.commonConnectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.connectors.PersonalExamConnector;

public interface PersonalExamConnectorTest extends PersonalExamConnector {

    PersonalExam getByNameAndStudentId(String examName, Long studentId);
}
