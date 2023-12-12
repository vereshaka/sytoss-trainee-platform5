package com.sytoss.lessons.controllers.viewModel;

import com.sytoss.domain.bom.lessons.Exam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractExamSummary {

    protected Exam exam;
}
