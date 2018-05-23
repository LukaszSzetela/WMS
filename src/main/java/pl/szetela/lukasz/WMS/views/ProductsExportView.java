package pl.szetela.lukasz.WMS.views;

import org.apache.poi.ss.usermodel.Workbook;
import pl.szetela.lukasz.WMS.dto.ProductDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ProductsExportView extends AbstractExportView {

    private List<ProductDto> products;
    private String reportName;

    public ProductsExportView(List<ProductDto> products, String reportName) {
        this.products = products;
        this.reportName = reportName;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        setFileName(reportName, response);
        sheet = workbook.createSheet("Product Details");
        sheet.setDefaultColumnWidth(30);
        setStyle(workbook);
        enterHeaders();
        enterData();
    }

    @Override
    String[] getHeaders() {
        return new String[]{"Ordinal", "Id", "Category", "Subcategory", "Name", "Price", "Number", "Reserved number", "Minimum inventory level", "Reordel level"};
    }

    private void enterData() {
        AtomicReference<AtomicInteger> currentColumn = new AtomicReference<>(new AtomicInteger(0));
        AtomicInteger ordinalNumber = new AtomicInteger(1);
        products.forEach(y -> {
            row = sheet.createRow(rowNumber.incrementAndGet());
            createCell(currentColumn.get().getAndIncrement(), Double.valueOf(ordinalNumber.getAndIncrement()));
            createCell(currentColumn.get().getAndIncrement(), y.getProductId());
            createCell(currentColumn.get().getAndIncrement(), y.getCategory());
            createCell(currentColumn.get().getAndIncrement(), y.getSubcategory());
            createCell(currentColumn.get().getAndIncrement(), y.getName());
            createCell(currentColumn.get().getAndIncrement(), y.getPrice());
            createCell(currentColumn.get().getAndIncrement(), y.getNumber());
            createCell(currentColumn.get().getAndIncrement(), y.getReservedNumber());
            createCell(currentColumn.get().getAndIncrement(), y.getZB());
            createCell(currentColumn.get().getAndIncrement(), y.getZI());
            currentColumn.set(new AtomicInteger(0));
        });
    }
}
