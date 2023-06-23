package com.sytoss.lessons.connectors;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${users-url}", name = "personalExamConnector")
public interface PersonalExamConnector {

    @GetMapping("taskDomain/{taskDomainId}/isUsedNow")
    boolean taskDomainIsUsed(@PathVariable Long taskDomainId);
}

