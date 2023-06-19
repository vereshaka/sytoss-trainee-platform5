package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${users-url}", name = "personalExamConnector")
public interface PersonalExamConnector {

    @GetMapping("taskDomain/{taskDomainId}/personalExam")
    List<PersonalExam> findAllPersonalExamByTaskDomain(@PathVariable Long taskDomainId);
}

