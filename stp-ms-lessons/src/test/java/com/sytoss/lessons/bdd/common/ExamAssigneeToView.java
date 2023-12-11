package com.sytoss.lessons.bdd.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamAssigneeToView {

    private String studentId;
    private String assigneeId;
    private String groupId;

    public ExamAssigneeToView(Map<String, String> input) {
        if (input.get("studentId") != null) {
            studentId = input.get("studentId");
        } else if (input.get("groupId") != null) {
            groupId = input.get("groupId");
        }
        setAssigneeId(input.get("assigneeId"));
    }
}
