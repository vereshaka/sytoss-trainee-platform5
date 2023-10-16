package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.exceptions.business.notfound.*;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.ExamAssigneeConvertor;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToGroupAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService extends AbstractService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    private final UserConnector userConnector;

    private final PersonalExamConnector personalExamConnector;

    private final ExamAssigneeConvertor examAssigneeConvertor;

    private final ExamAssigneeConnector examAssigneeConnector;

    private final DisciplineConnector disciplineConnector;

    private final ExamAssigneeToConnector examAssigneeToConnector;

    private final ExamAssigneeService examAssigneeService;

    public Exam save(Exam exam) {
        exam.setTeacher((Teacher) getCurrentUser());
        List<Task> distinctTasks = new ArrayList<>();
        for (Task task : exam.getTasks()) {
            if (!distinctTasks.stream().map(Task::getId).toList().contains(task.getId())) {
                distinctTasks.add(task);
            }
        }
        if (exam.getTasks().size() == exam.getNumberOfTasks() && distinctTasks.size() < exam.getNumberOfTasks()) {
            exam.setNumberOfTasks(distinctTasks.size());
        }
        exam.setTasks(distinctTasks);
        //TODO: yevgenyv: re think it
        exam.setDiscipline(exam.getTopics().get(0).getDiscipline());
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);
        if (exam.getDiscipline() != null) {
            examDTO.setDiscipline(disciplineConnector.getReferenceById(exam.getDiscipline().getId()));
        }
        examDTO = examConnector.save(examDTO);
        examConvertor.fromDTO(examDTO, exam);
        return exam;
    }

    public List<Exam> findExams() {
        AbstractUser abstractUser = getCurrentUser();

        if (abstractUser instanceof Teacher) {
            List<ExamDTO> examDTOList = examConnector.findByTeacherIdOrderByCreationDateDesc(abstractUser.getId());

            return examDTOList.stream().map((examDTO) -> {
                Exam exam = new Exam();
                examConvertor.fromDTO(examDTO, exam);
                return exam;
            }).toList();
        } else {
            log.warn("User type was not valid when try to get exams by teacher id!");
            throw new UserNotIdentifiedException("User type not teacher!");
        }
    }

    public Exam getById(Long examId) {
        try {
            ExamDTO taskDTO = examConnector.getReferenceById(examId);
            Exam exam = new Exam();
            examConvertor.fromDTO(taskDTO, exam);
            return exam;
        } catch (EntityNotFoundException e) {
            throw new ExamNotFoundException(examId);
        }
    }

    public ExamAssignee reschedule(ScheduleModel scheduleModel, Long examAssigneeId) {
        ExamAssigneeDTO examToUpdateAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
        ExamAssignee examToUpdateAssignee = new ExamAssignee();
        examAssigneeConvertor.fromDTO(examToUpdateAssigneeDTO, examToUpdateAssignee);
        examToUpdateAssignee.setRelevantTo(scheduleModel.getRelevantTo());
        examToUpdateAssignee.setRelevantFrom(scheduleModel.getRelevantFrom());
        examAssigneeConvertor.toDTO(examToUpdateAssignee, examToUpdateAssigneeDTO);

        examToUpdateAssigneeDTO = examAssigneeConnector.save(examToUpdateAssigneeDTO);
        examAssigneeConvertor.fromDTO(examToUpdateAssigneeDTO, examToUpdateAssignee);

        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setExamAssignee(examToUpdateAssignee);
        personalExamConnector.reschedule(examConfiguration);

        return examToUpdateAssignee;
    }

    public List<Exam> getExamsByTaskId(Long taskId) {
        List<ExamDTO> examDTOList = examConnector.findByTasks_Id(taskId);

        if (Objects.isNull(examDTOList)) {
            throw new TaskNotFoundException(taskId);
        }

        return examDTOList.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
    }

    public List<Exam> getExamsByTaskDomainId(Long taskDomainId) {
        List<ExamDTO> examDTOList = examConnector.findByTasks_TaskDomain_Id(taskDomainId);

        if (Objects.isNull(examDTOList)) {
            throw new TaskDomainNotFoundException(taskDomainId);
        }

        return examDTOList.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
    }

    public List<Exam> getExamsByDiscipline(Long disciplineId) {
        List<ExamDTO> examDTOList = examConnector.findByTopics_Discipline_Id(disciplineId);

        if (Objects.isNull(examDTOList)) {
            throw new DisciplineNotFoundException(disciplineId);
        }

        return examDTOList.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
    }

    public void deleteById(Long examId) {
        Exam exam = getById(examId);
        examConnector.deleteById(exam.getId());
    }

    public void deleteAssignTopicToExam(TopicDTO topicDTO) {
        List<ExamDTO> examDTOList = examConnector.findByTopicsId(topicDTO.getId());
        examDTOList.forEach(examDTO -> {
            examDTO.getTopics().remove(topicDTO);
            examConnector.save(examDTO);
        });
    }

    public void deleteAssignTaskToExam(TaskDTO taskDTO) {
        List<ExamDTO> examDTOList = examConnector.findByTasks_Id(taskDTO.getId());
        examDTOList.forEach(examDTO -> {
            examDTO.getTasks().remove(taskDTO);
            examConnector.save(examDTO);
        });
    }

    public Exam delete(Long examId) {
        Exam exam = getById(examId);
        List<ExamAssigneeDTO> examAssigneeDTOS = examAssigneeConnector.getAllByExam_Id(examId);
        examAssigneeService.deleteAllByExamId(examId);
        personalExamConnector.deletePersonalExamsByExamAssigneeId(examAssigneeDTOS.stream().map(ExamAssigneeDTO::getId).toList());


        examConnector.deleteById(exam.getId());
        return exam;
    }

    public Exam assign(Long examId, ExamAssignee examAssignee) {
        ExamDTO examDTO = examConnector.getReferenceById(examId);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setExam(examDTO);
        examAssigneeConvertor.toDTO(examAssignee, examAssigneeDTO);
        examAssigneeDTO = examAssigneeConnector.save(examAssigneeDTO);
        examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
        Exam exam = new Exam();
        examConvertor.fromDTO(examDTO, exam);

        for (Group group : examAssignee.getGroups()) {
            ExamToGroupAssigneeDTO examToGroupAssigneeDTO = new ExamToGroupAssigneeDTO();
            examToGroupAssigneeDTO.setGroupId(group.getId());
            examToGroupAssigneeDTO.setParent(examAssigneeDTO);
            examAssigneeToConnector.save(examToGroupAssigneeDTO);
            List<Student> students = userConnector.getStudentOfGroup(group.getId());
            for (Student student : students) {
                try {
                    //TODO: yevgenyv: fix me ASAP
                    personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
                } catch (Exception e) {
                    //TODO: yevgenyv: need to re think return answer
                    log.error("Could not create a personal exam for student", e);
                }
            }
        }
        for (Student student : examAssignee.getStudents()) {
            ExamToStudentAssigneeDTO assigneeToDto = new ExamToStudentAssigneeDTO();
            assigneeToDto.setStudentId(student.getId());
            assigneeToDto.setParent(examAssigneeDTO);
            examAssigneeToConnector.save(assigneeToDto);
            try {
                personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
            } catch (Exception e) {
                //TODO: yevgenyv: need to re think return answer
                log.error("Could not create a personal exam for student", e);
            }
        }
        return exam;
    }

    public List<ExamAssignee> returnExamAssignees(Long examId) {
        ExamDTO examDTO = examConnector.getReferenceById(examId);
        Exam exam = new Exam();
        examConvertor.fromDTO(examDTO, exam);
        return exam.getExamAssignees();
    }

    public ExamAssignee returnExamAssigneeById(Long examAssigneeId) {
        ExamAssigneeDTO examAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
        ExamAssignee examAssignee = new ExamAssignee();
        examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
        return examAssignee;
    }

    public void createGroupExamsOnStudent(Long groupId, Student student) {
        List<ExamToGroupAssigneeDTO> examToGroupAssigneeDTOList = examAssigneeToConnector.findByGroupIdOrderByParent_RelevantFromDesc(groupId);
        examToGroupAssigneeDTOList.forEach(examToGroupAssigneeDTO -> {
            ExamAssignee examAssignee = new ExamAssignee();
            examAssigneeConvertor.fromDTO(examToGroupAssigneeDTO.getParent(), examAssignee);
            if (new Date().before(examAssignee.getRelevantFrom())) {
                try {
                    ExamDTO examDTO = examConnector.getReferenceById(examToGroupAssigneeDTO.getParent().getExam().getId());
                    Exam exam = new Exam();
                    examConvertor.fromDTO(examDTO, exam);
                    personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
                } catch (Exception exception) {
                    log.warn("Could not create group exam on student: {}", exception.getMessage());
                }
            }
        });
    }

    public ExamReportModel getReportInfo(Long examAssigneeId) {
        try {
            ExamReportModel examReportModel = new ExamReportModel();
            ExamAssigneeDTO examAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
            examReportModel.setRelevantFrom(examAssigneeDTO.getRelevantFrom());
            examReportModel.setRelevantTo(examAssigneeDTO.getRelevantTo());

            ExamDTO examDTO = examConnector.findByExamAssignees_Id(examAssigneeId);
            List<TaskReportModel> tasks = examDTO.getTasks().stream().map(taskDTO -> {
                TaskReportModel task = new TaskReportModel();
                task.setId(taskDTO.getId());
                task.setQuestion(taskDTO.getQuestion());
                return task;
            }).toList();

            examReportModel.setExamName(examDTO.getName());
            examReportModel.setTasks(tasks);
            examReportModel.setAmountOfTasks(examDTO.getNumberOfTasks());
            examReportModel.setMaxGrade(examDTO.getMaxGrade());
            return examReportModel;
        } catch (EntityNotFoundException exception) {
            log.warn("Exam assignee not found: {}", exception.getMessage());
            throw new ExamAssigneeNotFoundException(examAssigneeId);
        }
    }
}
