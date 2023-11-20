package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamsElement {
    private Exam exam;
    private PersonalExam personalExam;
}
