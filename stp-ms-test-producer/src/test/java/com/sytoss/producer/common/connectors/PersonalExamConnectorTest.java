package com.sytoss.producer.common.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.connectors.PersonalExamConnector;

public interface PersonalExamConnectorTest extends PersonalExamConnector {

    PersonalExam getByNameAndStudent_Uid(String examName, String studentId);
}
