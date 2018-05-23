package pl.szetela.lukasz.WMS.views;

import org.apache.poi.ss.usermodel.*;
import pl.szetela.lukasz.WMS.dto.OrderDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OrdersExportView extends AbstractExportView {

    private List<OrderDto> orders;
    private String reportName;

    public OrdersExportView(List<OrderDto> orders, String reportName) {
        this.orders = orders;
        this.reportName = reportName;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        setFileName(reportName, response);
        sheet = workbook.createSheet("Orders Details");
        sheet.setDefaultColumnWidth(30);
        setStyle(workbook);
        enterHeaders();
        enterData();
    }

    @Override
    String[] getHeaders() {
        return new String[]{"Ordinal", "Status", "Order date", "Sub total", "Tax", "Tax rate", "Total cost", "Orderer", "Orderer - email", "Executor", "Executor - email"};
    }

    private void enterData() {
        AtomicReference<AtomicInteger> currentColumn = new AtomicReference<>(new AtomicInteger(0));
        AtomicInteger ordinalNumber = new AtomicInteger(1);
        orders.forEach(y -> {
            row = sheet.createRow(rowNumber.incrementAndGet());
            createCell(currentColumn.get().getAndIncrement(), Double.valueOf(ordinalNumber.getAndIncrement()));
            createCell(currentColumn.get().getAndIncrement(), y.getStatus());
            createCell(currentColumn.get().getAndIncrement(), y.getOrderDate().toString());
            createCell(currentColumn.get().getAndIncrement(), y.getSubTotal());
            createCell(currentColumn.get().getAndIncrement(), y.getTax());
            createCell(currentColumn.get().getAndIncrement(), y.getTaxRate());
            createCell(currentColumn.get().getAndIncrement(), y.getTotalCost());
            createCell(currentColumn.get().getAndIncrement(), y.getOrderer().getName());
            createCell(currentColumn.get().getAndIncrement(), y.getOrderer().getEmail());
            createCell(currentColumn.get().getAndIncrement(), String.format("%s %s", y.getExecutor().getFirstName(), y.getExecutor().getLastName()));
            createCell(currentColumn.get().getAndIncrement(), y.getExecutor().getEmail());
            currentColumn.set(new AtomicInteger(0));
        });
    }
}
