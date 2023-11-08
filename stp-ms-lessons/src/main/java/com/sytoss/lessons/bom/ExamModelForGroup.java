package com.sytoss.lessons.bom;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamModelForGroup {

    private Exam exam;

    private List<Group> groups;
}
