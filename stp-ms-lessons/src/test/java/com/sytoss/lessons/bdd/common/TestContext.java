package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class TestContext {

    private ResponseEntity<String> response;

    private int status;

    private ResponseEntity<List<Group>> listOfGroupResponse;

    private ResponseEntity<List<Topic>> listOfTopicResponse;

    private Long disciplineId;
}
