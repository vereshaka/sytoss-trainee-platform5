package com.sytoss.producer.writers;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Getter(AccessLevel.PROTECTED)
public abstract class BaseExcelWriter {

    private Workbook wb;

    private CellStyle label;

    private CellStyle value;

    private CellStyle date;

    private CellStyle tableHeader;

    private CellStyle notFill;

    protected void init() {
        wb = new XSSFWorkbook();

        label = createStyle(IndexedColors.WHITE, VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT, false);
        value = createStyle(IndexedColors.WHITE, VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT, false);
        date = createStyle(IndexedColors.WHITE, VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT, true);
        tableHeader = createStyle(IndexedColors.WHITE, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, false);
        notFill = createStyle(IndexedColors.YELLOW, VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT, false);
    }

    protected void createComment(Cell cell, Row row, String commentStr, XSSFSheet sheet) {
        CreationHelper factory = wb.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(row.getRowNum());
        anchor.setRow2(row.getRowNum() + 3);
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(commentStr);
        comment.setString(str);
        cell.setCellComment(comment);
    }

    private CellStyle createStyle(
            IndexedColors backgroundColor,
            VerticalAlignment verticalAlignment,
            HorizontalAlignment horizontalAlignment,
            Boolean isDate
    ) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setVerticalAlignment(verticalAlignment);
        if (isDate) {
            CreationHelper creationHelper = wb.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm"));
        }
        if (backgroundColor != IndexedColors.WHITE) {
            cellStyle.setFillForegroundColor(backgroundColor.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return cellStyle;
    }
}
