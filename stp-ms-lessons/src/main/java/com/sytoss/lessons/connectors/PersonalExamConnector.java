package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url = "${test-producer-url}", name = "personalExamConnector")
public interface PersonalExamConnector {

    @GetMapping("task-domain/{taskDomainId}/is-used-now")
    boolean taskDomainIsUsed(@PathVariable Long taskDomainId);

    @PostMapping("personal-exam/create")
    void create(ExamConfiguration configuration);

    @PostMapping("personal-exam/reschedule")
    void reschedule(ExamConfiguration configuration);

    @DeleteMapping("personal-exam/exam/delete")
    void deletePersonalExamsByExamAssigneeId(@RequestBody List<Long> examAssigneeIds);

    @PostMapping("personal-exam/task/update")
    void updateTask(Task task);

    @PostMapping("personal-exam/students")
    List<PersonalExam> getListOfPersonalExamByStudents(Long disciplineId,
                                                       List<Long> examAssignees,
                                                       List<Student> students);
}

