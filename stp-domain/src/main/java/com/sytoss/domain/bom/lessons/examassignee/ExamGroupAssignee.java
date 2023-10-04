package com.sytoss.domain.bom.lessons.examassignee;

import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExamGroupAssignee extends ExamAssignee {
    List<Group> groups = new ArrayList<>();
}
