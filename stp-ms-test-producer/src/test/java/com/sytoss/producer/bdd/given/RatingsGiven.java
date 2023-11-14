package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import com.sytoss.stp.test.common.DataTableCommon;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;

public class RatingsGiven extends TestProducerIntegrationTest {

    @Given("personal exams are exist")
    public void personalExamsAreExist(DataTable personalExamsData) {
        List<Map<String,String>> personalExamMapList = personalExamsData.asMaps();

        List<PersonalExam> personalExams = new ArrayList<>();
        List<Exam> exams = new ArrayList<>();
        List<ExamAssignee> examAssignees = new ArrayList<>();
        DataTableCommon dataTableCommon = new DataTableCommon();
        for(Map<String,String> personalExamMap : personalExamMapList){
            personalExams.add(dataTableCommon.mapPersonalExam(personalExamMap));
            String examAssigneeId = personalExamMap.get("examAssigneeId");
            if(examAssigneeId!=null){
                ExamAssignee examAssignee = new ExamAssignee();
                examAssignee.setId(Long.parseLong(examAssigneeId));
                String examId = personalExamMap.get("examId");
                if(examId!=null) {
                    Exam exam = exams.stream().filter(exam1 -> exam1.getId()==Long.parseLong(examId)).toList().get(0);
                    if(exam==null){
                        exam = new Exam();
                        exam.setId(Long.parseLong(examId));
                    }
                    examAssignee.setExam(exam);
                }
                examAssignees.add(examAssignee);
            }
        }

        when(().getByExamAssigneeId())

        for (PersonalExam personalExam : personalExamList) {
            if (getPersonalExamConnector().getByNameAndStudentUid(personalExam.getName(), personalExam.getStudent().getUid()) == null) {
                getPersonalExamConnector().save(personalExam);
            }
        }
    }
}
