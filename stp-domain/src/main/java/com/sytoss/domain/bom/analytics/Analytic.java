package com.sytoss.domain.bom.analytics;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Analytic extends AnalyticDrop {

    private Exam exam;

    private PersonalExam personalExam;
    private Date startDate;
}
