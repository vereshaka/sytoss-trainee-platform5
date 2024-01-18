package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.exceptions.business.notfound.*;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ExamReportModel;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.controllers.views.ExamAssigneesStatus;
import com.sytoss.lessons.convertors.ExamAssigneeConvertor;
import com.sytoss.lessons.convertors.ExamAssigneesStatusConverter;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExamService extends AbstractService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    private final UserConnector userConnector;

    private final PersonalExamConnector personalExamConnector;

    private final ExamAssigneeConvertor examAssigneeConvertor;

    private final ExamAssigneeConnector examAssigneeConnector;

    private final DisciplineConnector disciplineConnector;

    private final ExamAssigneeToConnector examAssigneeToConnector;

    private final TopicConnector topicConnector;

    private final TaskConnector taskConnector;

    private final AnalyticsService analyticsService;

    private final GroupReferenceConnector groupReferenceConnector;

    private final ExamAssigneesStatusConverter examAssigneesStatusConverter;

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
        final ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);

        examDTO.setTopics(new ArrayList<>());
        exam.getTopics().forEach(topic -> examDTO.getTopics().add(topicConnector.getReferenceById(topic.getId())));

        examDTO.setTasks(new ArrayList<>());
        exam.getTasks().forEach(task -> examDTO.getTasks().add(taskConnector.getReferenceById(task.getId())));

        if (exam.getDiscipline() != null) {
            examDTO.setDiscipline(disciplineConnector.getReferenceById(exam.getDiscipline().getId()));
        }
        ExamDTO result = examConnector.save(examDTO);
        examConvertor.fromDTO(result, exam);

        List<GroupReferenceDTO> groups = groupReferenceConnector.findByDisciplineId(exam.getDiscipline().getId());
        for (GroupReferenceDTO group : groups) {
            analyticsService.checkOrCreate(exam.getId(), exam.getDiscipline().getId(), group.getGroupId());
        }

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
        List<ExamDTO> examDTOList = examConnector.findByDiscipline_Id(disciplineId);
        examDTOList.sort((emp1, emp2) -> emp2.getCreationDate().compareTo(emp1.getCreationDate()));

        if (Objects.isNull(examDTOList)) {
            throw new DisciplineNotFoundException(disciplineId);
        }

        return examDTOList.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
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
        analyticsService.deleteByExam(examId);
        personalExamConnector.deletePersonalExamsByExamAssigneeId(exam.getExamAssignees().stream().map(ExamAssignee::getId).toList());

        ExamDTO dto = examConnector.getReferenceById(examId);
        dto.getExamAssignees().forEach(item -> {
            item.getExamAssigneeToDTOList().forEach(ea2item -> examAssigneeToConnector.delete(ea2item));
            item.setExamAssigneeToDTOList(new ArrayList<>());
            examAssigneeConnector.delete(item);
        });

        dto.setExamAssignees(new ArrayList<>());
        dto.setTopics(new ArrayList<>());
        dto.setTasks(new ArrayList<>());

        examConnector.save(dto);

        examConnector.deleteById(examId);
        return exam;
    }

    public Exam assign(Long examId, ExamAssignee examAssignee) {
        ExamDTO examDTO = examConnector.getReferenceById(examId);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssignee.getExam().setId(examId);
        examAssigneeConvertor.toDTO(examAssignee, examAssigneeDTO);
        examAssigneeDTO.setExam(examDTO);
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
//            for (Student student : students) {
//                try {
//                    //TODO: yevgenyv: fix me ASAP
//                    personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
//                } catch (Exception e) {
//                    //TODO: yevgenyv: need to re think return answer
//                    log.error("Could not create a personal exam for student", e);
//                }
//            }
            analyticsService.checkOrCreate(examId, examDTO.getDiscipline().getId(), students);
        }
        for (Student student : examAssignee.getStudents()) {
            ExamToStudentAssigneeDTO assigneeToDto = new ExamToStudentAssigneeDTO();
            assigneeToDto.setStudentId(student.getId());
            assigneeToDto.setParent(examAssigneeDTO);
            examAssigneeToConnector.save(assigneeToDto);
//            try {
//                personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
//            } catch (Exception e) {
//                //TODO: yevgenyv: need to re think return answer
//                log.error("Could not create a personal exam for student", e);
//            }
        }
        analyticsService.checkOrCreate(examId, exam.getDiscipline().getId(), examAssignee.getStudents());

        createPersonalExams(exam, examAssignee);

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

    public List<ExamAssignee> findExamAssignees() {
        AbstractUser abstractUser = getCurrentUser();
        if (abstractUser instanceof Teacher) {
            List<ExamDTO> examDTOList = examConnector.findByTeacherIdOrderByCreationDateDesc(abstractUser.getId());
            List<Long> examDTOIds = examDTOList.stream().map(ExamDTO::getId).toList();
            List<ExamAssigneeDTO> examAssigneeDTOList = examAssigneeConnector.findByExam_IdInOrderByRelevantFromDesc(examDTOIds);
            return examAssigneeDTOList.stream().map(examAssigneeDTO -> {
                ExamAssignee examAssignee = new ExamAssignee();
                examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
                return examAssignee;
            }).toList();
        } else {
            log.warn("User type was not valid when try to get exams by teacher id!");
            throw new UserNotIdentifiedException("User type not teacher!");
        }
    }

    public ExamReportModel getReportInfo(Long examAssigneeId) {
        try {
            ExamReportModel examReportModel = new ExamReportModel();
            ExamAssigneeDTO examAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
            examReportModel.setRelevantFrom(examAssigneeDTO.getRelevantFrom());
            examReportModel.setRelevantTo(examAssigneeDTO.getRelevantTo());

            ExamDTO examDTO = examConnector.findByExamAssignees_Id(examAssigneeId);
            examReportModel.setExamName(examDTO.getName());
            examReportModel.setAmountOfTasks(examDTO.getNumberOfTasks());
            examReportModel.setMaxGrade(examDTO.getMaxGrade());
            return examReportModel;
        } catch (EntityNotFoundException exception) {
            log.warn("Exam assignee not found: {}", exception.getMessage());
            throw new ExamAssigneeNotFoundException(examAssigneeId);
        }
    }

    public List<Exam> getExamsByTopic(Long topicId) {
        TopicDTO topic = new TopicDTO();
        topic.setId(topicId);
        List<ExamDTO> examDTOS = examConnector.getAllByTopicsContaining(topic);
        return examDTOS.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
    }

    public Exam getExamByExamAssignee(Long examAssigneeId) {
        ExamDTO examDTO = examConnector.findByExamAssignees_Id(examAssigneeId);
        if (examDTO != null) {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }
        throw new ExamNotFoundException("examAssigneeId: " + examAssigneeId);
    }

    public List<Exam> findExamsByStudent(Long disciplineId) {
        AbstractUser abstractUser = getCurrentUser();
        List<Long> groups = userConnector.getGroupsOfStudent().stream().map(Group::getId).toList();
        List<ExamDTO> examDTOList = examConnector.findByDiscipline_Id(disciplineId);

        if (Objects.isNull(examDTOList)) {
            throw new DisciplineNotFoundException(disciplineId);
        }

        List<ExamDTO> filteredExams = new ArrayList<>();
        for (ExamDTO examDTO : examDTOList) {
            for (ExamAssigneeDTO examAssigneeDTO : examDTO.getExamAssignees().stream().filter(examAssigneeDTO -> Date.from(Instant.now()).after(examAssigneeDTO.getRelevantFrom())).toList()) {
                List<ExamAssigneeToDTO> examAssigneeToDTOS = examAssigneeDTO.getExamAssigneeToDTOList();
                for (ExamAssigneeToDTO examAssigneeToDTO : examAssigneeToDTOS) {
                    if (examAssigneeToDTO instanceof ExamToStudentAssigneeDTO) {
                        if (Objects.equals(((ExamToStudentAssigneeDTO) examAssigneeToDTO).getStudentId(), abstractUser.getId())
                                && !filteredExams.stream().map(ExamDTO::getId).toList().contains(examDTO.getId())) {
                            filteredExams.add(examDTO);
                        }
                    }
                    else {
                        if (groups.contains(((ExamToGroupAssigneeDTO) examAssigneeToDTO).getGroupId())
                                && !filteredExams.stream().map(ExamDTO::getId).toList().contains(examDTO.getId())) {
                            filteredExams.add(examDTO);
                        }
                    }
                }
            }

        }

        return filteredExams.stream().map(examDTO -> {
            Exam exam = new Exam();
            examConvertor.fromDTO(examDTO, exam);
            return exam;
        }).toList();
    }

    public ExamAssigneesStatus getExamAssigneesStatusByExamId(Long examId) {
        Optional<ExamDTO> examDTOOpt = examConnector.findById(examId);
        return examDTOOpt.map(examDTO -> {
            ExamAssigneesStatus examAssigneesStatus = new ExamAssigneesStatus();
            examAssigneesStatusConverter.fromDTO(examDTO, examAssigneesStatus);
            return examAssigneesStatus;
        }).orElseThrow(() -> new ExamNotFoundException(examId));
    }

    public void update(Long examId, Exam exam) {
        Optional<ExamDTO> examDTOOpt = examConnector.findById(examId);

        examDTOOpt.ifPresentOrElse(examDTO -> {
                    examDTO.setName(exam.getName());
                    examDTO.setNumberOfTasks(exam.getNumberOfTasks());
                    examDTO.setMaxGrade(exam.getMaxGrade());
                    examConnector.save(examDTO);

                    examConvertor.fromDTO(examDTO, exam);
                    exam.getExamAssignees().forEach(examAssignee -> createPersonalExams(exam, examAssignee));
                },
                () -> {
                    throw new ExamNotFoundException(exam.getId());
                });
    }

    // todo: check and sync with method com.sytoss.lessons.services.ExamService.assign
    // error handling? -> look for solution to return error details
    // POST or PUT? -> both
    // send single student entity or list? -> single, use futures
    // User.id or uid -> use uid
    private void createPersonalExams(Exam exam, ExamAssignee examAssignee) {
            List<Student> students = new ArrayList<>(examAssignee.getStudents());
            examAssignee.getGroups().forEach(g ->
                    students.addAll(userConnector.getStudentOfGroup(g.getId())));

            students.forEach(student -> {
                try {
                    personalExamConnector.create(new ExamConfiguration(exam, examAssignee, student));
                } catch (Exception e) {
                    log.error("Unable to create a personal exam for student", e);
                }
            });
    }
}
