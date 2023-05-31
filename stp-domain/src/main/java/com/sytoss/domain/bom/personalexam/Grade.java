package com.sytoss.domain.bom.personalexam;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {

    @JsonView(PersonalExam.Public.class)
    private float value;

    @JsonView(PersonalExam.Public.class)
    private String comment;
}
