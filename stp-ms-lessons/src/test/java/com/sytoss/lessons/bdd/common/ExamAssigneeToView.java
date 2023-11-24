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

    public ExamAssigneeToView(Map<String, String> input) {
        setAssigneeId(input.get("assigneeId"));
        setStudentId(input.get("studentId"));
    }
}