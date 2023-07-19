package com.sytoss.producer.bdd.common;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TestProducerDetails {
    private ResponseEntity<String> response;

    private int statusCode;

    private ResponseEntity<Question> firstTaskResponse;

    private ResponseEntity<PersonalExam> personalExamResponse;

    private String studentId;

    @Getter
    @Setter
    private Map<String, String> taskMapping = new HashMap<>();
}