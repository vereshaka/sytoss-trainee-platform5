package com.sytoss.producer.bdd.common;

import com.sytoss.domain.bom.personalexam.Question;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class TestContext {

    private ResponseEntity<String> response;

    private int statusCode;

    private ResponseEntity<Question> firstTaskResponse;
}
