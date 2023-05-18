package com.sytoss.producer.commonConnectors;

import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.connectors.PersonalExamConnector;

public interface PersonalExamConnectorForTest extends PersonalExamConnector {

    PersonalExam getByNameAndStudentId(String examName, Long studentId);
}
