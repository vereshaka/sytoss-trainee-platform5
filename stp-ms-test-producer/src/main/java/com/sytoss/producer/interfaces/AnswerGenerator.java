package com.sytoss.producer.interfaces;

import com.sytoss.domain.bom.exceptions.business.TaskCountNotValidException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AnswerGenerator implements GenerateAnswersStrategy {

    private final Random ANSWER_GENERATOR = new Random();

    @Override
    public List<Answer> generateAnswers(int numberOfTasks, List<Task> tasks) {
        Map<Long, List<Task>> map = tasks.stream()
                .flatMap(task -> task.getTopics().stream().map(topic -> new AbstractMap.SimpleEntry<>(topic.getId(), task)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ));
        List<Long> shuffledTopics = new ArrayList<>(map.keySet());
        Collections.shuffle(shuffledTopics);

        List<Task> selectedTasks = new ArrayList<>();

        int totalCount = 0;
        for (List<Task> taskList : map.values()) {
            totalCount += taskList.size();
        }

        if (numberOfTasks > totalCount) {
            throw new TaskCountNotValidException("Number of tasks can not be greater that tasks size!");
        }

        int totalTasks = 0;
        while (totalTasks < numberOfTasks) {
            for (Long topicId : shuffledTopics) {
                if (totalTasks >= numberOfTasks) break;
                List<Task> currentTasks = map.get(topicId);
                if (!currentTasks.isEmpty()) {
                    int randomTaskIndex = ANSWER_GENERATOR.nextInt(currentTasks.size());
                    Task selectedTask = currentTasks.remove(randomTaskIndex);
                    selectedTasks.add(selectedTask);
                    totalTasks++;
                }
            }
        }

        return convertToAnswers(selectedTasks);
    }

    private List<Answer> convertToAnswers(List<Task> tasks) {
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Answer answer = new Answer();
            answer.setId((long) (i + 1));
            answer.setStatus(AnswerStatus.NOT_STARTED);
            answer.setTask(tasks.get(i));
            answers.add(answer);
        }

        return answers;
    }
}
