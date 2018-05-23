package pl.szetela.lukasz.WMS.views;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractExportView extends AbstractXlsView {

    protected AtomicInteger rowNumber = new AtomicInteger(0);
    protected Sheet sheet;
    protected Row row;
    protected Cell cell;
    protected CellStyle style;
    protected Font font;

    abstract String[] getHeaders();

    protected void setStyle(Workbook workbook) {
        style = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
    }

    protected void enterHeaders() {
        AtomicInteger currentColumn = new AtomicInteger(0);
        row = sheet.createRow(rowNumber.get());
        Arrays.stream(getHeaders()).forEach(y -> {
            Cell cell = row.createCell(currentColumn.getAndIncrement());
            cell.setCellValue(y);
        });
    }

    protected void setFileName(String fileName, HttpServletResponse response) {
        String fullName = fileName + "_" + createFileNumber();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fullName + ".xls\"");
    }

    protected void createCell(int currentColumn, String value) {
        cell = row.createCell(currentColumn);
        cell.setCellValue(value);
    }

    protected void createCell(int currentColumn, Long value) {
        cell = row.createCell(currentColumn);
        cell.setCellValue(value);
    }

    protected void createCell(int currentColumn, Integer value) {
        cell = row.createCell(currentColumn);
        cell.setCellValue(value);
    }

    protected void createCell(int currentColumn, Double value) {
        cell = row.createCell(currentColumn);
        cell.setCellValue(value);
    }

    private String createFileNumber() {
        return String.valueOf(LocalDate.now().getYear()) + String.valueOf(LocalDate.now().getMonthValue()) +
                String.valueOf(LocalDate.now().getDayOfMonth());
    }
}
