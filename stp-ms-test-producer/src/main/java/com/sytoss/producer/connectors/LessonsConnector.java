package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ExamReportModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${lessons-url}", name = "lessonsConnector")
public interface LessonsConnector {

    @GetMapping(value = "assignee/{examAssigneeId}/report")
    ExamReportModel getReportInfo(@PathVariable Long examAssigneeId);

    @PostMapping("analytics")
    void updateAnalytic(@RequestBody Analytics newItem);

    @GetMapping("exam/assignee/{examAssigneeId}")
    Exam getExamByAssignee(@PathVariable(name = "examAssigneeId") Long examAssigneeId);
}
