package com.sytoss.domain.bom;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Grade {

    private Long id;

    @JsonView(PersonalExam.Public.class)
    private float value;

    @JsonView(PersonalExam.Public.class)
    private String comment;
}
