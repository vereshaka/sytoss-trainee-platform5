package com.sytoss.stp.test.common;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.analytics.RatingModel;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import liquibase.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTableCommon {

    @DataTableType
    public QueryResult mapQueryResult(DataTable table) {
        List<Map<String, String>> tableMaps = table.entries();
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        for (Map<String, String> tableMap : tableMaps) {
            Map<String, Object> resultMap = new HashMap<>();
            for (String key : tableMap.keySet().stream().toList()) {
                resultMap.put(key, StringUtil.isNumeric(tableMap.get(key)) && tableMap.get(key).length() < 10 ? Integer.parseInt(tableMap.get(key)) : tableMap.get(key));
            }
            resultMaps.add(resultMap);
        }
        QueryResult queryResult = new QueryResult();

        List<String> header = tableMaps.get(0).keySet().stream().toList();
        queryResult.setHeader(header);
        for (Map<String, Object> row : resultMaps) {
            queryResult.addValues(row);
        }

        return queryResult;
    }

    @DataTableType
    public Discipline mapDiscipline(Map<String, String> entry) {
        Discipline discipline = new Discipline();
        discipline.setName(entry.get("discipline"));

        if (entry.containsKey("teacherId")) {
            Teacher teacher = new Teacher();
            teacher.setId(Long.parseLong(entry.get("teacherId")));
            discipline.setTeacher(teacher);
        } /*else {
            Teacher teacher = new Teacher();
            teacher.setId(Long.parseLong(entry.get("teacherId")));
            discipline.setTeacher(teacher);
            discipline.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
        }*/
        return discipline;
    }

    @DataTableType
    public PersonalExam mapPersonalExam(Map<String, String> entry) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setName(entry.get("examName"));
        Task task = new Task();
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(entry.get("task domain"));
        task.setTaskDomain(taskDomain);
        Answer answer = new Answer();
        answer.setTask(task);
        answer.setStatus(AnswerStatus.NOT_STARTED);
        personalExam.getAnswers().add(answer);


        String temp = entry.get("disciplineId");
        if(temp!=null){
            Discipline discipline = new Discipline();
            discipline.setId(Long.parseLong(temp));
            personalExam.setDiscipline(discipline);
        }

        temp = entry.get("personalExamId");
        if(temp!=null){
            personalExam.setId(temp);
        }

        temp = entry.get("examAssigneeId");
        if(temp!=null){
            personalExam.setExamAssigneeId(Long.parseLong(temp));
        }

        temp = entry.get("studentId");
        if(temp!=null){
            Student student = new Student();
            student.setId(Long.parseLong(temp));
            personalExam.setStudent(student);
        }

        temp = entry.get("summaryGrade");
        if(temp!=null){
            personalExam.setSummaryGrade(Double.parseDouble(temp));
        }

        return personalExam;
    }

    @DataTableType
    public Topic mapTopic(Map<String, String> entry) {
        Topic topic = new Topic();
        Discipline discipline = new Discipline();
        topic.setName(entry.get("topic"));
        discipline.setName(entry.get("discipline"));
        topic.setDiscipline(discipline);
        return topic;
    }

    @DataTableType
    public Group mapGroups(Map<String, String> entry) {
        Long groupId = Long.parseLong(entry.get("group"));
        Group group = new Group();
        group.setId(groupId);
        if (entry.containsKey("discipline")) {
            Long disciplineId = Long.parseLong(entry.get("discipline"));
            Discipline discipline = new Discipline();
            discipline.setId(disciplineId);
            group.setDiscipline(discipline);
        }
        return group;
    }

    @DataTableType
    public TaskDomain mapTaskDomains(Map<String, String> entry) {
        TaskDomain taskDomain = new TaskDomain();
        Discipline discipline = new Discipline();
        discipline.setName(entry.get("discipline"));
        taskDomain.setName(entry.get("task domain"));
        taskDomain.setDiscipline(discipline);
        String id = entry.get("id");
        if (id != null) {
            taskDomain.setId(Long.parseLong(id.substring(1)));
        }
        return taskDomain;
    }

 /*   @DataTableType
    public Group mapGroups(Map<String, String> entry) {
        Long groupId = Long.parseLong(entry.get("group"));
        Group group = new Group();
        group.setId(groupId);
        return group;
    }*/

    @DataTableType
    public Task mapTasks(Map<String, String> entry) {
        String question = entry.get("task");
        Task task = new Task();
        task.setQuestion(question);
        return task;
    }

    @DataTableType
    public Answer mapAnswer(Map<String, String> params) {
        Answer answer = new Answer();

        if (params.containsKey("answerId")) {
            answer.setId(Long.valueOf(params.get("answerId")));
        }
        if (params.containsKey("answer")) {
            answer.setValue(params.get("answer"));
        }
        if (params.containsKey("status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("status")));
        }
        if (params.containsKey("task status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("task status")));
        }

        Task task = new Task();
        if (params.containsKey("taskId")) {
            task.setId(Long.valueOf(params.get("taskId")));
        }
        if (params.containsKey("question")) {
            task.setQuestion(params.get("question"));
        }
        if (params.containsKey("etalon")) {
            task.setEtalonAnswer(params.get("etalon"));
        }
        if (params.containsKey("coef")) {
            task.setCoef(Double.parseDouble(params.get("coef")));
        }
        if (params.containsKey("task")) {
            task.setQuestion(params.get("task"));
            answer.setTask(task);
        }
        TaskDomain taskDomain = new TaskDomain();
        if (params.containsKey("taskDomainId")) {
            taskDomain.setId(Long.valueOf(params.get("taskDomainId")));
            task.setTaskDomain(taskDomain);
        }

        Grade grade = new Grade();
        if (params.containsKey("grade") && params.get("grade") != null) {
            grade.setValue(Float.parseFloat(params.get("grade")));
            grade.setComment(params.get("comment"));
            answer.setGrade(grade);
        }

        Topic topic = new Topic();
        if (params.containsKey("listOfSubjects")) {
            topic.setName(params.get("listOfSubjects"));
            task.setTopics(List.of(topic));
        }

        if (params.containsKey("task status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("task status")));
        }

        if (params.containsKey("script")) {
            taskDomain.setDatabaseScript(params.get("script"));
            task.setTaskDomain(taskDomain);
            answer.setTask(task);
        }
        answer.setTask(task);

        return answer;
    }

    @DataTableType
    public Student mapStudents(Map<String, String> entry) {
        Student student = new Student();
        student.setFirstName(entry.get("firstName"));
        student.setLastName(entry.get("lastName"));
        return student;
    }

    @DataTableType
    public AnalyticsElement mapAnalyticsElement(Map<String, String> entry) {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        String temp = entry.get("disciplineId");
        if(temp!=null){
            analyticsElement.setDisciplineId(Long.parseLong(temp));
        }

       temp = entry.get("examId");
        if(temp!=null){
            analyticsElement.setExamId(Long.parseLong(temp));
        }

        temp = entry.get("studentId");
        if(temp!=null){
            analyticsElement.setStudentId(Long.parseLong(temp));
        }

        temp = entry.get("personalExamId");
        if(temp!=null){
            analyticsElement.setPersonalExamId(temp);
        }

        temp = entry.get("grade");
        if(temp!=null){
            analyticsElement.setGrade(Double.parseDouble(temp));
        }

        temp = entry.get("timeSpent");
        if(temp!=null){
            analyticsElement.setTimeSpent(Long.parseLong(temp));
        }

        temp = entry.get("examAssigneeId");
        if(temp!=null){
            analyticsElement.setExamAssigneeId(Long.parseLong(temp));
        }

        return analyticsElement;
    }

    @DataTableType
    public RatingModel mapRatings(Map<String, String> entry) {
        RatingModel ratingModel = new RatingModel();
        ratingModel.setStudentId(Long.valueOf(entry.get("studentId")));
        ratingModel.setAvgGrade(Double.parseDouble(entry.get("avgGrade")));
        ratingModel.setAvgTimeSpent(Math.round(Double.parseDouble(entry.get("avgTimeSpent"))));
        return ratingModel;
    }
}
