package com.sytoss.producer.interfaces;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;

import java.util.List;

public interface GenerateAnswersStrategy {

    List<Answer> generateAnswers(int numberOfTasks, List<Task> tasks);
}
