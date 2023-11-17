package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class PersonalExamGiven extends LessonsIntegrationTest {

    @Given("^personal exams for migration exist$")
    public void personalExamsForMigrationExist(DataTable dataTable) throws ParseException {
        List<Student> students = new ArrayList<>();
        Group group = new Group();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        if (getTestExecutionContext().getDetails().getGroupId() == null) {
            group.setId(1L);
        } else {
            group.setId(getTestExecutionContext().getDetails().getGroupId().get(0));
        }
        List<Map<String, String>> personalExamMaps = dataTable.asMaps();
        List<PersonalExam> personalExams = new ArrayList<>();
        for (Map<String, String> personalExamMap : personalExamMaps) {
            PersonalExam personalExam = new PersonalExam();

            Discipline discipline = new Discipline();
            discipline.setId((Long) getTestExecutionContext().replaceId(personalExamMap.get("disciplineId")));
            personalExam.setDiscipline(discipline);

            personalExam.setExamAssigneeId(Long.parseLong(getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId")).toString()));

            Student student = new Student();
            student.setId(Long.parseLong(getTestExecutionContext().replaceId(personalExamMap.get("studentId")).toString()));
            student.setPrimaryGroup(group);
            personalExam.setStudent(student);

            String personalExamId = personalExamMap.get("personalExamId").trim().replace("*", "");
            personalExam.setId(personalExamId);

            if (personalExamMap.get("summaryGrade") != null) {
                personalExam.setSummaryGrade(Double.parseDouble(personalExamMap.get("summaryGrade").trim()));
            }

            String startDate = personalExamMap.get("startDate");
            if (startDate != null) {
                personalExam.setStartedDate(sdf.parse(startDate));
            }

            personalExams.add(personalExam);
            if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                student.setPrimaryGroup(group);
                students.add(student);
            }

            when(getPersonalExamConnector()
                    .getListOfPersonalExamByExamAssigneeId(personalExam.getExamAssigneeId()))
                    .thenReturn(personalExams.stream().filter(personalExam1 -> personalExam1.getExamAssigneeId().equals(personalExam.getExamAssigneeId())
                            && personalExam1.getStudent().getId().equals(personalExam.getStudent().getId())).toList());
        }

        when(getUserConnector().getStudentOfGroup(group.getId())).thenReturn(students);
        getTestExecutionContext().getDetails().setPersonalExams(personalExams);
    }

    @Given("^personal exams exist$")
    public void personalExist(List<PersonalExam> personalExams) {
        for (PersonalExam personalExam : personalExams) {
            for (Answer answer : personalExam.getAnswers()) {
                if (getTestExecutionContext().getDetails().getDisciplineId() != null) {
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
        }

        getTestExecutionContext().getDetails().setPersonalExams(personalExams);
    }
}
