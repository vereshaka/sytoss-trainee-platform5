package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.ExamReportModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${lessons-url}", name = "examAssigneeConnector")
public interface ExamAssigneeConnector {

    @GetMapping(value = "assignee/{examAssigneeId}/report")
    ExamReportModel getReportInfo(@PathVariable Long examAssigneeId);
}
