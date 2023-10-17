package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {

    @JsonView({PersonalExam.Public.class, PersonalExam.TeacherOnly.class})
    private Long id;

    @JsonView({PersonalExam.Public.class, PersonalExam.TeacherOnly.class})
    private String name;

    private Discipline discipline;

    private int countOfStudents;
}
