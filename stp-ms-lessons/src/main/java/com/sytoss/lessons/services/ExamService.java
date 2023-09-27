package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Exam> saveExamForGroup(Exam exam, List<Group> groups) {
        List<Exam> exams = new ArrayList<>();

        for(Group group : groups){
            exam.setTeacher((Teacher) getCurrentUser());
            List<Task> distinctTasks = new ArrayList<>();
            for(Task task : exam.getTasks()){
                if(!distinctTasks.stream().map(Task::getId).toList().contains(task.getId())){
                    distinctTasks.add(task);
                }
            }
            if(exam.getTasks().size() == exam.getNumberOfTasks() && distinctTasks.size() < exam.getNumberOfTasks()){
                exam.setNumberOfTasks(distinctTasks.size());
            }
            exam.setTasks(distinctTasks);
            exam.setGroup(group);
            //TODO: yevgenyv: re think it
            exam.setDiscipline(exam.getTopics().get(0).getDiscipline());
            ExamDTO examDTO = new ExamDTO();
            examConvertor.toDTO(exam, examDTO);
            examDTO = examConnector.save(examDTO);
            examConvertor.fromDTO(examDTO, exam);
            List<Student> students = userConnector.getStudentOfGroup(exam.getGroup().getId());
            for (Student student: students){
                try {
                    personalExamConnector.create(new ExamConfiguration(exam, student));
                } catch (Exception e) {
                    //TODO: yevgenyv: need to re think return answer
                    log.error("Could not create a personal exam for student", e);
                }
            }
            exams.add(exam);
        }

        return exams;
    }

    public List<Exam> findExams() {
        AbstractUser abstractUser = getCurrentUser();

        if (abstractUser instanceof Teacher) {
            List<ExamDTO> examDTOList = examConnector.findByTeacherId(abstractUser.getId());

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

    public Exam reschedule(ScheduleModel scheduleModel, Long examId) {
        ExamDTO examToUpdateDTO = examConnector.getReferenceById(examId);
        Exam examToUpdate = new Exam();
        examConvertor.fromDTO(examToUpdateDTO, examToUpdate);
        examToUpdate.setRelevantTo(scheduleModel.getRelevantTo());
        examToUpdate.setRelevantFrom(scheduleModel.getRelevantFrom());
        examConvertor.toDTO(examToUpdate, examToUpdateDTO);

        examToUpdateDTO = examConnector.save(examToUpdateDTO);
        examConvertor.fromDTO(examToUpdateDTO, examToUpdate);

        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setExam(examToUpdate);

        personalExamConnector.reschedule(examConfiguration);

        return examToUpdate;
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
        examConnector.deleteById(exam.getId());
        personalExamConnector.deletePersonalExamsByExamId(examId);
        return exam;
    }

    public List<Exam> saveExamForStudents(Exam exam, List<Student> students) {
        List<Exam> exams = new ArrayList<>();

        exam.setTeacher((Teacher) getCurrentUser());
        List<Task> distinctTasks = new ArrayList<>();
        for(Task task : exam.getTasks()){
            if(!distinctTasks.stream().map(Task::getId).toList().contains(task.getId())){
                distinctTasks.add(task);
            }
        }
        if(exam.getTasks().size() == exam.getNumberOfTasks() && distinctTasks.size() < exam.getNumberOfTasks()){
            exam.setNumberOfTasks(distinctTasks.size());
        }
        exam.setTasks(distinctTasks);
        exam.setDiscipline(exam.getTopics().get(0).getDiscipline());
        ExamDTO examDTO = new ExamDTO();
        for (Student student: students){
            exam.setGroup(student.getPrimaryGroup());
            examConvertor.toDTO(exam, examDTO);
            examDTO = examConnector.save(examDTO);
            examConvertor.fromDTO(examDTO, exam);
            try {
                personalExamConnector.create(new ExamConfiguration(exam, student));
            } catch (Exception e) {
                log.error("Could not create a personal exam for student", e);
            }
        }
        exams.add(exam);

        return exams;
    }
}
