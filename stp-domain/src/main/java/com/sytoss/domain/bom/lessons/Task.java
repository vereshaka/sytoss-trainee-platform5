package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Task {

    private Long id;

    @JsonView({PersonalExam.Public.class, PersonalExam.TeacherOnly.class})
    private String question;

    @JsonView({PersonalExam.TeacherOnly.class})
    private String etalonAnswer;

    @JsonView({PersonalExam.TeacherOnly.class})
    private String checkAnswer;

    private TaskDomain taskDomain;

    private List<Topic> topics = new ArrayList<>();

    @JsonView({PersonalExam.TeacherOnly.class})
    private List<TaskCondition> taskConditions = new ArrayList<>();

    private Long imageId;

    private Double coef;

    private String requiredCommand;

    private boolean isSortingRelevant;

    @JsonView({PersonalExam.TeacherOnly.class})
    private String code;

    private Date createDate;
}
