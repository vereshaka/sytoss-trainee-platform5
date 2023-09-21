package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${test-producer-url}", name = "personalExamConnector")
public interface PersonalExamConnector {

    @GetMapping("task-domain/{taskDomainId}/is-used-now")
    boolean taskDomainIsUsed(@PathVariable Long taskDomainId);

    @PostMapping("personal-exam/create")
    void create(ExamConfiguration configuration);

    @PostMapping("personal-exam/reschedule")
    void reschedule(ExamConfiguration configuration);

    @DeleteMapping("personal-exam/exam/{examId}/delete")
    void deletePersonalExamsByExamId(@PathVariable("examId") Long examId);
}

