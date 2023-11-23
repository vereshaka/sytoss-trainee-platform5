package com.sytoss.domain.bom.analytics;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalyticFull {

    @JsonView({AnalyticFullView.class})
    private Discipline discipline;

    @JsonView({AnalyticFullView.class})
    private Student student;

    @JsonView({AnalyticFullView.class})
    private SummaryGrade studentGrade;

    @JsonView({AnalyticFullView.class})
    private List<Test> tests;

    public static class AnalyticFullView {

    }
}
