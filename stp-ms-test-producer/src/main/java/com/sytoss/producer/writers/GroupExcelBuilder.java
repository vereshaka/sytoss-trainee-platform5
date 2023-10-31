package com.sytoss.producer.writers;

import com.sytoss.domain.bom.personalexam.PersonalExamReportModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@Scope("prototype")
public class GroupExcelBuilder extends BaseExcelWriter {

    private final String sheetName = "Group exam result";

    private final List<String> groupTableLabels = List.of("Group", "StudentName");

    private final Integer groupTableStartIndex = 1;

    public Workbook createExcelReportByGroup(List<PersonalExamReportModel> reportModels) {
        init();
        getWb().createSheet(sheetName);

        Map<String, List<PersonalExamReportModel>> examsByName = reportModels.stream().collect(Collectors.groupingBy(PersonalExamReportModel::getExamName));
        Map<Long, List<PersonalExamReportModel>> examsByStudent = reportModels.stream().collect(Collectors.groupingBy(PersonalExamReportModel::getStudentId));

        writeGroupTable(examsByName, examsByStudent);
        return getWb();
    }

    private void writeGroupTable(
            Map<String, List<PersonalExamReportModel>> examsByName,
            Map<Long, List<PersonalExamReportModel>> examsByStudent
    ) {
        writeGroupTableHeader(examsByName);
        writeGroupTableContent(examsByName, examsByStudent);
    }

    private void writeGroupTableHeader(Map<String, List<PersonalExamReportModel>> examsByName) {
        XSSFSheet sheet = (XSSFSheet) getWb().getSheet(sheetName);
        Row headerRow = sheet.createRow(groupTableStartIndex);
        Row headerSubRow = sheet.createRow(groupTableStartIndex + 1);
        int cellIndex = 0;
        for (; cellIndex < groupTableLabels.size(); cellIndex++) {
            writeCell(headerRow, cellIndex, getTableHeader(), groupTableLabels.get(cellIndex));
            sheet.autoSizeColumn(cellIndex);
            CellRangeAddress cellAddresses = new CellRangeAddress(groupTableStartIndex, groupTableStartIndex + 1, cellIndex, cellIndex);
            sheet.addMergedRegion(cellAddresses);
        }

        for (String examName : examsByName.keySet()) {
            writeCell(headerRow, cellIndex, getValue(), examName);
            CellRangeAddress cellAddresses = new CellRangeAddress(groupTableStartIndex, groupTableStartIndex, cellIndex, cellIndex + 1);
            sheet.addMergedRegion(cellAddresses);
            writeCell(headerSubRow, cellIndex, getTableHeader(), "Summary grade");
            writeCell(headerSubRow, cellIndex + 1, getTableHeader(), "Start date");
            sheet.autoSizeColumn(cellIndex);
            sheet.autoSizeColumn(cellIndex + 1);
            cellIndex += 2;
        }

        writeCell(headerSubRow, cellIndex, getTableHeader(), "Summary");
        sheet.autoSizeColumn(cellIndex);
    }

    private void writeGroupTableContent(
            Map<String, List<PersonalExamReportModel>> examsByName,
            Map<Long, List<PersonalExamReportModel>> examsByStudent
    ) {
        XSSFSheet sheet = (XSSFSheet) getWb().getSheet(sheetName);
        int tableStartIndex = groupTableStartIndex + 2;
        for (Long studentId: examsByStudent.keySet()) {
            int contentValueIndex = 0;
            Row contentRow = sheet.createRow(tableStartIndex++);
            List<PersonalExamReportModel> reportModels = examsByStudent.get(studentId);
            writeCell(contentRow, contentValueIndex, getValue(), reportModels.get(0).getGroupName());
            sheet.autoSizeColumn(contentValueIndex++);
            writeCell(contentRow, contentValueIndex, getValue(), reportModels.get(0).getStudentName());
            sheet.autoSizeColumn(contentValueIndex++);
            float summary = 0f;
            for (String examName : examsByName.keySet()) {
                Optional<PersonalExamReportModel> reportModel = reportModels.stream().filter(personalExamReportModel -> Objects.equals(personalExamReportModel.getExamName(), examName)).findFirst();

                if (reportModel.isPresent()) {
                    writeCell(contentRow, contentValueIndex, getValue(), reportModel.get().getSummary());
                    summary += reportModel.get().getSummary();
                    if (reportModel.get().getStartDate() != null) {
                        writeCell(contentRow, contentValueIndex + 1, getDate(), reportModel.get().getStartDate());
                    } else {
                        writeCell(contentRow, contentValueIndex + 1, getDate(), "Not started");
                    }

                    sheet.autoSizeColumn(contentValueIndex + 1);
                }

                contentValueIndex += 2;
            }
            writeCell(contentRow, contentValueIndex, getValue(), summary);
        }
    }
}
