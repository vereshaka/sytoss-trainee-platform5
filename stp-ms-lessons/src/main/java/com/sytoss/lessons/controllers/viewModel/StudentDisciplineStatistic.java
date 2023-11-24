package com.sytoss.lessons.controllers.viewModel;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDisciplineStatistic {

    private Discipline discipline;

    private Student student;

    private SummaryGrade summaryGrade;

    private List<StudentTestExecutionSummary> tests;

}
