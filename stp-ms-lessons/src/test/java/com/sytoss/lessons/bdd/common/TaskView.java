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
public class TaskView {

    private String question;
    private String answer;
    private String id;
    private String topics;

    public TaskView(Map<String, String> input) {
        setQuestion(input.get("question"));
        setId(input.get("id"));
        setTopics(input.get("topics"));
        setAnswer(input.get("answer"));
    }
}
