package com.sytoss.producer.writers;

import com.sytoss.domain.bom.lessons.ExamReportModel;
import com.sytoss.domain.bom.lessons.TaskReportModel;
import com.sytoss.domain.bom.personalexam.AnswerReportModel;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExamReportModel;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@Scope("prototype")
public class ExcelBuilder extends BaseExcelWriter {

    private final Integer labelStartIndex = 0;

    private final Integer tableStartIndex = 7;

    private Integer tableValueStartIndex = 0;

    private final List<String> tableLabels = List.of("Name", "Group", "Student name", "Email", "Summary");

    public Workbook createExcelReportByExamAssignee(
            ExamReportModel examReportModel, List<PersonalExamReportModel> personalExams
    ) {
        init();
        XSSFSheet sheet = (XSSFSheet) getWb().createSheet("Exam result");
        writeHeader(sheet, examReportModel);
        autoSizeHeader(sheet);
        writeTable(sheet, examReportModel, personalExams);
        return getWb();
    }

    private void writeHeader(
            XSSFSheet sheet,
            ExamReportModel report
    ) {
        int startHeaderIndex = 1;

        Row row = sheet.createRow(startHeaderIndex++);
        writeHeaderLabelWithValue(row, "Exam name", report.getExamName(), getValue(), labelStartIndex);
        writeHeaderLabelWithValue(row, "Max grade", report.getMaxGrade(), getValue(), labelStartIndex + 6);

        row = sheet.createRow(startHeaderIndex++);
        writeHeaderLabelWithValue(row, "From", report.getRelevantFrom(), getDate(), labelStartIndex);
        writeHeaderLabelWithValue(row, "Amount of task", report.getAmountOfTasks(), getValue(), labelStartIndex + 6);

        row = sheet.createRow(startHeaderIndex);
        writeHeaderLabelWithValue(row, "To", report.getRelevantTo(), getDate(), labelStartIndex);
        writeHeaderLabelWithValue(row, "Tasks in test", report.getTasks().size(), getValue(), labelStartIndex + 6);
    }

    private void writeTable(
            XSSFSheet sheet,
            ExamReportModel report,
            List<PersonalExamReportModel> personalExams
    ) {
        writeTableHeader(sheet, report);
        writeTableContent(sheet, report.getTasks(), personalExams);
    }

    private void writeTableHeader(XSSFSheet sheet, ExamReportModel report) {
        Row tableRow = sheet.createRow(tableStartIndex);
        Row tableSubRow = sheet.createRow(tableStartIndex + 1);
        int cellIndex = labelStartIndex;
        for (; cellIndex < tableLabels.size(); cellIndex++) {
            writeCell(tableRow, cellIndex, getTableHeader(), tableLabels.get(cellIndex));
            sheet.autoSizeColumn(cellIndex);

            CellRangeAddress cellAddresses = new CellRangeAddress(tableStartIndex, tableStartIndex + 1, cellIndex, cellIndex);
            sheet.addMergedRegion(cellAddresses);
        }

        for (TaskReportModel task : report.getTasks()) {
            CellRangeAddress cellAddresses = new CellRangeAddress(tableStartIndex, tableStartIndex, cellIndex, cellIndex + 2);
            writeCell(tableRow, cellIndex, getValue(), task.getQuestion());
            sheet.addMergedRegion(cellAddresses);

            writeCell(tableSubRow, cellIndex, getTableHeader(), "Answer Time");
            writeCell(tableSubRow, cellIndex + 1, getTableHeader(), "System Grade");
            writeCell(tableSubRow, cellIndex + 2, getTableHeader(), "Teacher Grade");

            sheet.autoSizeColumn(cellIndex);
            sheet.autoSizeColumn(cellIndex + 1);
            sheet.autoSizeColumn(cellIndex + 2);

            cellIndex += 3;
        }
    }

    private void writeTableContent(XSSFSheet sheet, List<TaskReportModel> tasksInExam, List<PersonalExamReportModel> personalExams) {
        int tableContentStartIndex = tableStartIndex + 2;

        for (PersonalExamReportModel personalExam : personalExams) {
            tableValueStartIndex = 0;
            Row contentRow = sheet.createRow(tableContentStartIndex++);
            writeCell(contentRow, tableValueStartIndex, getValue(), personalExam.getExamName());
            sheet.autoSizeColumn(tableValueStartIndex++);
            writeCell(contentRow, tableValueStartIndex, getValue(), personalExam.getGroupName());
            sheet.autoSizeColumn(tableValueStartIndex++);
            writeCell(contentRow, tableValueStartIndex, getValue(), personalExam.getStudentName());
            sheet.autoSizeColumn(tableValueStartIndex++);
            writeCell(contentRow, tableValueStartIndex, getValue(), personalExam.getEmail());
            sheet.autoSizeColumn(tableValueStartIndex++);
            writeCell(contentRow, tableValueStartIndex++, getValue(), personalExam.getSummary());
            writePersonalExamGrades(personalExam, tasksInExam, contentRow, sheet);
        }
    }

    private void writePersonalExamGrades(
            PersonalExamReportModel personalExam, List<TaskReportModel> tasksInExam,
            Row contentRow, XSSFSheet sheet
    ) {
        if (personalExam.getPersonalExamStatus() == PersonalExamStatus.FINISHED
            || personalExam.getPersonalExamStatus() == PersonalExamStatus.REVIEWED
        ) {
            for (TaskReportModel task : tasksInExam) {
                Optional<AnswerReportModel> findAnswer = personalExam.getAnswers().stream().filter(answer -> Objects.equals(answer.getTask().getQuestion(), task.getQuestion())).findFirst();
                if (findAnswer.isPresent() && findAnswer.get().getAnswerStatus() == AnswerStatus.GRADED) {
                    writeCell(contentRow, tableValueStartIndex, getValue(), findAnswer.get().getTimeSpent() + "s");
                    Cell cell = writeCell(contentRow, tableValueStartIndex + 1, getValue(), findAnswer.get().getSystemGrade().getValue());

                    if (!Objects.equals(findAnswer.get().getSystemGrade().getComment(), "")) {
                        createComment(cell, contentRow, findAnswer.get().getSystemGrade().getComment(), sheet);
                    }

                    writeCell(contentRow, tableValueStartIndex + 2, getValue(), findAnswer.get().getTeacherGrade().getValue());
                } else {
                    writeCell(contentRow, tableValueStartIndex, getNotFill(), "");
                    writeCell(contentRow, tableValueStartIndex + 1, getNotFill(), "");
                    writeCell(contentRow, tableValueStartIndex + 2, getNotFill(), "");
                }
                tableValueStartIndex += 3;
            }
        }
    }

    private void autoSizeHeader(XSSFSheet sheet) {
        sheet.autoSizeColumn(labelStartIndex);
        sheet.autoSizeColumn(labelStartIndex + 1);
        sheet.autoSizeColumn(labelStartIndex + 6);
        sheet.autoSizeColumn(labelStartIndex + 7);
    }

    private void writeHeaderLabelWithValue(
            Row row, String label, Object value,
            CellStyle valueStyle, Integer labelStartIndex
    ) {
        writeCell(row, labelStartIndex, getLabel(), label);
        writeCell(row, labelStartIndex + 1, valueStyle, value);
    }

    private Cell writeCell(
            Row row, Integer cellIndex,
            CellStyle cellStyle, Object value
    ) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellStyle(cellStyle);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value.toString());
        }

        return cell;
    }

    public void write(String path, Workbook workbook) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            log.warn("An error occurred while writing file: {}", e.getMessage());
        }
    }
}
