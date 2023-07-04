package com.sytoss.producer.common.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.connectors.PersonalExamConnector;

public interface PersonalExamConnectorTest extends PersonalExamConnector {

    PersonalExam getByNameAndStudentUid(String examName, String studentId);
}
