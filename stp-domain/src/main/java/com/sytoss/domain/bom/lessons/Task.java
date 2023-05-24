package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Task {

    private Long id;

    @JsonView(PersonalExam.Public.class)
    private String question;

    private String etalonAnswer;

    private TaskDomain taskDomain;

    private List<Topic> topics;
}
