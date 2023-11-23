package com.sytoss.domain.bom.analytics;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private Exam exam;

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private PersonalExam personalExam;

}
