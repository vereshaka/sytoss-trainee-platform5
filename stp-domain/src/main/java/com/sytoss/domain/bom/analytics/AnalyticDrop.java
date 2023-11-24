package com.sytoss.domain.bom.analytics;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AnalyticDrop {

    protected Discipline discipline;
    protected Student student;
    protected AnalyticGrade grade;
}
