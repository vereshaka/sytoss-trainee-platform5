package com.sytoss.domain.bom.analytics;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AnalyticDrop {

    private Discipline discipline;
    private Student student;

    private AnaliticGrade grade;
}
