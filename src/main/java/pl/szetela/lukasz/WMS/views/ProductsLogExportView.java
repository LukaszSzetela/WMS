package pl.szetela.lukasz.WMS.views;

import org.apache.poi.ss.usermodel.Workbook;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ProductsLogExportView extends AbstractExportView {

    private List<ProductLogDto> productsLog;
    private String reportName;

    public ProductsLogExportView(List<ProductLogDto> productsLog, String reportName) {
        this.productsLog = productsLog;
        this.reportName = reportName;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        setFileName(reportName, response);
        sheet = workbook.createSheet("Products Log Details");
        sheet.setDefaultColumnWidth(30);
        setStyle(workbook);
        enterHeaders();
        enterData();
    }

    @Override
    String[] getHeaders() {
        return new String[]{"Ordinal", "Id", "Category", "Subcategory", "Name", "Date", "Price", "Number", "Product Id"};
    }

    private void enterData() {
        AtomicReference<AtomicInteger> currentColumn = new AtomicReference<>(new AtomicInteger(0));
        AtomicInteger ordinalNumber = new AtomicInteger(1);
        productsLog.forEach(y -> {
            row = sheet.createRow(rowNumber.incrementAndGet());
            createCell(currentColumn.get().getAndIncrement(), Double.valueOf(ordinalNumber.getAndIncrement()));
            createCell(currentColumn.get().getAndIncrement(), y.getId());
            createCell(currentColumn.get().getAndIncrement(), y.getCategory());
            createCell(currentColumn.get().getAndIncrement(), y.getSubcategory());
            createCell(currentColumn.get().getAndIncrement(), y.getName());
            createCell(currentColumn.get().getAndIncrement(), y.getDate().toString());
            createCell(currentColumn.get().getAndIncrement(), y.getPrice());
            createCell(currentColumn.get().getAndIncrement(), y.getNumber());
            createCell(currentColumn.get().getAndIncrement(), y.getProductId());
            currentColumn.set(new AtomicInteger(0));
        });
    }
}
