package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {

    @JsonView(PersonalExam.Public.class)
    private double value;

    @JsonView(PersonalExam.Public.class)
    private String comment;
}
