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
public class ExamAssigneeView {

    private String relevantFrom;
    private String relevantTo;
    private String examId;
    private String id;

    public ExamAssigneeView(Map<String, String> input) {
        setRelevantFrom(input.get("relevantFrom"));
        setRelevantTo(input.get("relevantTo"));
        setExamId(input.get("examId"));
        setId(input.get("id"));
    }
}