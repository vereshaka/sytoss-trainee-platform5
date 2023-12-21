package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.PersonalExamByStudentsModel;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PersonalExamGiven extends LessonsIntegrationTest {

    @Given("^personal exams for migration exist$")
    public void personalExamsForMigrationExist(DataTable dataTable) throws ParseException {
        List<Student> students = new ArrayList<>();
        List<Map<String, String>> personalExamMaps = dataTable.asMaps();
        List<PersonalExam> personalExams = new ArrayList<>();
        List<Long> disciplineIds = new ArrayList<>();
        List<ExamAssigneeDTO> examAssigneeDTOS = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        String firstDisciplineKey = null;
        for (Map<String, String> personalExamMap : personalExamMaps) {
            PersonalExam personalExam = new PersonalExam();

            Discipline discipline = new Discipline();
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            String disciplineKey = getTestExecutionContext().replaceId(personalExamMap.get("disciplineId")).toString();
            if(firstDisciplineKey==null){
                firstDisciplineKey=disciplineKey;
            }
            if (disciplineKey == getTestExecutionContext().replaceId(personalExamMap.get("disciplineId"))) {
                disciplineDTO.setName(disciplineKey);
                disciplineDTO.setTeacherId(1L);
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
                discipline.setId(disciplineDTO.getId());
                getTestExecutionContext().registerId(disciplineKey, discipline.getId());
            } else {
                discipline.setId((Long) getTestExecutionContext().replaceId(personalExamMap.get("disciplineId")));
            }
            if (!disciplineIds.contains(discipline.getId())) {
                disciplineIds.add(discipline.getId());
            }

            personalExam.setDiscipline(discipline);

            String examAssigneeKey = getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId")).toString();
            ExamAssigneeDTO examAssigneeDTO;
            if (examAssigneeKey == getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId"))) {
                examAssigneeDTO = new ExamAssigneeDTO();
                String examKey = getTestExecutionContext().replaceId("examId").toString();
                ExamDTO examDTO = new ExamDTO();
                if (examKey == getTestExecutionContext().replaceId("examId")) {
                    examDTO.setName(examKey);
                    examDTO.setDiscipline(disciplineDTO);
                    examDTO.setTeacherId(1L);
                    examDTO = getExamConnector().save(examDTO);
                } else {
                    examDTO = getExamConnector().getReferenceById(Long.parseLong(examKey));
                }
                examAssigneeDTO.setExam(examDTO);
                examAssigneeDTO = getExamAssigneeConnector().save(examAssigneeDTO);
                examAssigneeDTOS.add(examAssigneeDTO);
                getTestExecutionContext().registerId(examAssigneeKey, examAssigneeDTO.getId());
                examAssigneeKey = getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId")).toString();
            } else {
                examAssigneeDTO = getExamAssigneeConnector().findById(Long.parseLong(examAssigneeKey)).orElse(null);
            }

            if (!examAssigneeDTOS.stream().map(ExamAssigneeDTO::getId).toList().contains(Long.parseLong(examAssigneeKey))) {
                examAssigneeDTOS.add(examAssigneeDTO);
            }

            personalExam.setExamAssigneeId(Long.parseLong(getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId")).toString()));

            Student student = new Student();
            student.setId(Long.parseLong(getTestExecutionContext().replaceId(personalExamMap.get("studentId")).toString()));

            Group group = new Group();
            if (personalExamMap.get("groupId") != null) {
                if(personalExamMap.get("isExcluded") == null || Objects.equals(personalExamMap.get("isExcluded"), "false")){
                    String groupKey = getTestExecutionContext().replaceId(personalExamMap.get("groupId")).toString();
                    if (groupKey != getTestExecutionContext().replaceId(personalExamMap.get("groupId"))) {
                        group.setId(Long.parseLong(groupKey));
                        group.setName(groupKey);
                    }
                    if (!groups.stream().map(Group::getId).toList().contains(group.getId())) {
                        groups.add(group);
                    }
                }
                else{
                    continue;
                }

            } else {
                group.setId(1L);
                group.setName("1");
            }


            student.setPrimaryGroup(group);
            personalExam.setStudent(student);

            String personalExamId = personalExamMap.get("personalExamId").trim().replace("*", "");
            personalExam.setId(personalExamId);

            personalExam.summary();

            if (personalExamMap.get("summaryGrade") != null) {
                personalExam.setSummaryGrade(Double.parseDouble(personalExamMap.get("summaryGrade").trim()));
            }

            String startDate = personalExamMap.get("startDate");
            if (startDate != null) {
                personalExam.setStartedDate(sdf.parse(startDate));
            }

            String personalExamStatus = personalExamMap.get("status");
            if(personalExamStatus.equals(PersonalExamStatus.IN_PROGRESS.toString())){
                personalExam.start();
            }else if(personalExamStatus.equals(PersonalExamStatus.FINISHED.toString())){
                personalExam.finish();
            }else if(personalExamStatus.equals(PersonalExamStatus.REVIEWED.toString())){
                personalExam.review();
            }


            personalExams.add(personalExam);
            if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                student.setPrimaryGroup(group);
                students.add(student);
            }
        }

        for (Long disciplineId : disciplineIds) {
            List<Long> examAssigneeIds = examAssigneeDTOS.stream().filter(examAssigneeDTO -> Objects.equals(examAssigneeDTO.getExam().getDiscipline().getId(), disciplineId)).map(ExamAssigneeDTO::getId).toList();
            if (!examAssigneeIds.isEmpty()) {
                PersonalExamByStudentsModel personalExamByStudentsModel = new PersonalExamByStudentsModel();
                personalExamByStudentsModel.setDisciplineId(disciplineId);
                personalExamByStudentsModel.setExamAssignees(examAssigneeIds);
                personalExamByStudentsModel.setStudents(students.stream().filter(student -> groups.stream().map(Group::getId).toList().contains(student.getPrimaryGroup().getId())).toList());
                if(disciplineId==Long.parseLong(getTestExecutionContext().replaceId(firstDisciplineKey).toString())){
                    when(getPersonalExamConnector().getListOfPersonalExamByStudents(any(PersonalExamByStudentsModel.class)))
                            .thenReturn(personalExams.stream().filter(personalExam -> examAssigneeIds.contains(personalExam.getExamAssigneeId())).toList());

                }
            }
        }
        for (Group group : groups) {
            when(getUserConnector().getStudentOfGroup(group.getId()))
                    .thenReturn(students.stream()
                            .filter(student -> student.getPrimaryGroup().getId().equals(group.getId())).toList());
        }

        getTestExecutionContext().getDetails().setPersonalExams(personalExams);
    }

    @Given("^personal exam exists$")
    public void personalExist(List<PersonalExam> personalExams) {
        for (PersonalExam personalExam : personalExams) {
            for (Answer answer : personalExam.getAnswers()) {
                DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
                TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(answer.getTask().getTaskDomain().getName(), disciplineDTO.getId());
                if (taskDomainDTO == null) {
                    taskDomainDTO = new TaskDomainDTO();
                    taskDomainDTO.setName(answer.getTask().getTaskDomain().getName());
                    taskDomainDTO.setDiscipline(disciplineDTO);
                }
                taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
                TaskDomain taskDomain = new TaskDomain();
                getTaskDomainConvertor().fromDTO(taskDomainDTO, taskDomain);
                answer.getTask().setTaskDomain(taskDomain);
            }
        }
        getTestExecutionContext().getDetails().setPersonalExams(personalExams);
    }
}
