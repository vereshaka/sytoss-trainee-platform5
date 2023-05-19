package com.sytoss.producer.bdd.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class TestContext {

    private ResponseEntity<String> response;
}
