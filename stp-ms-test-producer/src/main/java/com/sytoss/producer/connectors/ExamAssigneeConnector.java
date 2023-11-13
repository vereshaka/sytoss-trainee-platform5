package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.ExamReportModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${lessons-url}", name = "examAssigneeConnector")
public interface ExamAssigneeConnector {

    @GetMapping(value = "assignee/{examAssigneeId}/report")
    ExamReportModel getReportInfo(@PathVariable Long examAssigneeId);

    @GetMapping(value = "assignee/{examId}/all")
    List<ExamAssignee> getAllExamAssignees(@PathVariable Long examId);
}
