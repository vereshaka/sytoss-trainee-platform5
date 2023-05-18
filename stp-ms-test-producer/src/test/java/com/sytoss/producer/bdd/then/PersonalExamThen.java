package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.bom.*;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamThen extends CucumberIntegrationTest {

    @DataTableType
    public Answer mapAnswer(Map<String, String> entry) {
        Task task = new Task();
        task.setId(Long.parseLong(entry.get("taskId")));
        task.setQuestion(entry.get("question"));

        Grade grade = new Grade();
        grade.setValue(Float.parseFloat(entry.get("grade")));
        grade.setComment(entry.get("comment"));

        Answer answer = new Answer();
        answer.setTask(task);
        answer.setValue(entry.get("answer"));
        answer.setGrade(grade);
        answer.setStatus(AnswerStatus.valueOf(entry.get("status")));

        return answer;
    }

    @Then("summary grade should be {float}")
    public void summaryGrade(Float summaryGrade) throws JsonProcessingException {
        PersonalExam personalExam = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(summaryGrade, personalExam.getSummaryGrade());
    }

    @Then("response should return personal exam with exam name {string} and studentID {long} and date {word}")
    public void responseShouldReturnPersonalExam(String examName, Long studentId, String date, List<Answer> answers) throws JsonProcessingException {
        PersonalExam personalExam = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        PersonalExam personalExamToCompare = new PersonalExam();
        personalExamToCompare.setName(examName);
        personalExamToCompare.setStudentId(studentId);
        personalExam.setAnswers(answers);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            personalExam.setDate(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(personalExamToCompare.getName(), personalExam.getName());
        assertEquals(personalExamToCompare.getStudentId(), personalExam.getStudentId());
        assertEquals(personalExamToCompare.getDate(), personalExam.getDate());
        assertEquals(personalExamToCompare.getAnswers().size(), personalExam.getAnswers().size());

        for (int i = 0; i < personalExamToCompare.getAnswers().size() - 1; ++i) {
            assertEquals(personalExamToCompare.getAnswers().get(i).getValue(), personalExam.getAnswers().get(i).getValue());
            assertEquals(personalExamToCompare.getAnswers().get(i).getStatus(), personalExam.getAnswers().get(i).getStatus());
            assertEquals(personalExamToCompare.getAnswers().get(i).getGrade().getValue(), personalExam.getAnswers().get(i).getGrade().getValue());
            assertEquals(personalExamToCompare.getAnswers().get(i).getGrade().getComment(), personalExam.getAnswers().get(i).getGrade().getComment());
            assertEquals(personalExamToCompare.getAnswers().get(i).getTask().getQuestion(), personalExam.getAnswers().get(i).getTask().getQuestion());
        }
    }
}
