package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ExamReportModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${lessons-url}", name = "examAssigneeConnector")
public interface LessonsConnector {

    @GetMapping(value = "assignee/{examAssigneeId}/report")
    ExamReportModel getReportInfo(@PathVariable Long examAssigneeId);

    @PostMapping("analytics")
    void updateAnalytic(@RequestBody Analytic newItem);

    @GetMapping("exam/assignee/{examAssigneeId}")
    Exam getExamByAssignee(@PathVariable long examAssigneeId);
}
