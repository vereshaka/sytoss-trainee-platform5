package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${users-url}", name = "personalExamConnector")
public interface PersonalExamConnector {

    @GetMapping("task-domain/{taskDomainId}/is-used-now")
    boolean taskDomainIsUsed(@PathVariable Long taskDomainId);

    @PostMapping("personal-exam/create")
    void create(ExamConfiguration configuration);
}

