package com.sytoss.producer.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.PersonalExam;
import com.sytoss.producer.services.PersonalExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonalExamController {

    private final PersonalExamService personalExamService;

    @PostMapping("/personalExam/{id}/summary")
    @JsonView(PersonalExam.Public.class)
    public PersonalExam summary(@PathVariable(value = "id") String id) {
        return personalExamService.summary(id);
    }
}
