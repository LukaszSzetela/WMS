package pl.szetela.lukasz.WMS.views;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.models.Customer;
import pl.szetela.lukasz.WMS.utils.PdfUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class InvoiceView extends AbstractPdfView {

    private static final Font BOLD_FONT = FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD);
    private static final Font HEADERS_FONT = FontFactory.getFont(FontFactory.TIMES, 12, BaseColor.WHITE);
    private static final Font LOGO_FONT = FontFactory.getFont(FontFactory.TIMES_ROMAN, 34, Font.ITALIC);
    private OrderDto order;
    private Customer customer;
    private AtomicInteger ordinalStart = new AtomicInteger(0);
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public InvoiceView(OrderDto order, Customer customer) {
        this.order = order;
        this.customer = customer;
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String invoiceNumber = createInvoiceName();

        // FIXME add printing every next invoice from database data
        preparePdfContent(invoiceNumber, response, document);
    }

    private void preparePdfContent(String invoiceNumber, HttpServletResponse response, Document document) throws DocumentException {
        setFileInvoiceName(invoiceNumber, response);
        createHeaders(document, invoiceNumber);
        createSpaceLine(document);
        createAndFillSellerAndSenderTable(document);
        createAndFillProductTable(document);
        createSummary(document);
        createSpaceLine(document);
        createAndFillAmountPaidAndToPay(document);
        createSpaceLine(document);
        createAndFillSignature(document);
    }

    private void setFileInvoiceName(String invoiceNumber, HttpServletResponse response) {
        String invoiceName = "invoice_" + invoiceNumber;
        response.setHeader("Content-Disposition", "attachment; filename=\"" + invoiceName + ".pdf\"");
    }

    private void createHeaders(Document document, String invoiceNumber) throws DocumentException {
        document.add(PdfUtils.createRowParagraph("Invoice number", invoiceNumber, BOLD_FONT));
        String dateOfIssue = String.valueOf(order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        document.add(PdfUtils.createRowParagraph("Date of issue", dateOfIssue, BOLD_FONT));
        String paymentDate = String.valueOf(order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(14));
        Paragraph paymentDateAndLogoParagraph = PdfUtils.createRowParagraph("Payment date", paymentDate, BOLD_FONT);
        document.add(addLogoToPaymentAndLogoParagraph(paymentDateAndLogoParagraph));
        document.add(PdfUtils.createRowParagraph("Payment", "transfer", BOLD_FONT));
    }

    private void createSpaceLine(Document document) throws DocumentException {
        document.add(PdfUtils.createSpaceLine());
    }

    private void createAndFillSellerAndSenderTable(Document document) throws DocumentException {
        PdfPTable table = createTable(3);
        createSellerAndSenderTitleRow(table, "Seller", "Sender");
        createSellerAndSenderRow(table, "WMS - app", customer.getName());
        createSellerAndSenderRow(table, "payments@wms-app.pl", customer.getEmail());
        createSellerAndSenderRow(table, "NIP:  7251801126", "NIP: " + customer.getNip());
        createSellerAndSenderRow(table, "BANK", "REGON: " + customer.getRegon());
        createSellerAndSenderRow(table, "1020 1441 1852 1144 11111 66666", "");
        document.add(table);
    }

    private void createAndFillProductTable(Document document) throws DocumentException {
        PdfPTable table = createTable(8);
        createAndFillTableHeaders(table);
        fillProductTable(table);
        fillTableSummary(table);
        document.add(table);
    }

    private void createSummary(Document document) throws DocumentException {
        document.add(PdfUtils.createRightAlignLine("Net value: " + order.getSubTotal() + " PLN", BOLD_FONT));
        document.add(PdfUtils.createRightAlignLine("Vat: " + decimalFormat.format(customer.getVatRate() * 100) + "%", BOLD_FONT));
        document.add(PdfUtils.createRightAlignLine("Shipping: " + order.getShippingCost() + " PLN", BOLD_FONT));
        document.add(PdfUtils.createRightAlignLine("Gross Value: " + order.getTotalCost() + " PLN", BOLD_FONT));
    }

    private void createAndFillAmountPaidAndToPay(Document document) throws DocumentException {
        document.add(PdfUtils.createRowParagraph("Amount paid", "0 PLN", BOLD_FONT, 20));
        createSpaceLine(document);
        document.add(PdfUtils.createRowParagraph("To pay", order.getTotalCost() + " PLN", BOLD_FONT, 20));
    }

    private void createAndFillSignature(Document document) throws DocumentException {
        document.add(PdfUtils.createRightAlignLine("Name and surname of the issuer", BOLD_FONT, 20));
        document.add(PdfUtils.createRightAlignLine("Admin of WMS - App"));
    }

    private Paragraph addLogoToPaymentAndLogoParagraph(Paragraph paymentDateAndLogoParagraph) {
        Chunk glue = new Chunk(new VerticalPositionMark());
        paymentDateAndLogoParagraph.add(glue);
        paymentDateAndLogoParagraph.add(new Phrase("WMS - App \u00a9", LOGO_FONT));
        return paymentDateAndLogoParagraph;
    }

    private void createSellerAndSenderTitleRow(PdfPTable table, String sellerData, String senderData) {
        table.addCell(PdfUtils.createBoldWithoutBorderCell(sellerData, BOLD_FONT));
        table.addCell(PdfUtils.emptyCell());
        table.addCell(PdfUtils.createBoldWithoutBorderCell(senderData, BOLD_FONT));
    }

    private void createSellerAndSenderRow(PdfPTable table, String sellerData, String senderData) {
        table.addCell(PdfUtils.createWithoutBorderCell(sellerData));
        table.addCell(PdfUtils.emptyCell());
        table.addCell(PdfUtils.createWithoutBorderCell(senderData));
    }

    private PdfPTable createTable(int columnNumber) {
        PdfPTable table = new PdfPTable(columnNumber);
        setProperties(table);
        return table;
    }

    private void setProperties(PdfPTable sellerAndSenderData) {
        sellerAndSenderData.setSpacingBefore(50);
        sellerAndSenderData.setWidthPercentage(100.0f);
    }

    private void createAndFillTableHeaders(PdfPTable table) {
        table.addCell(PdfUtils.createPdfTableCell("Ordinal", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("Name", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("Number", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("Net Price", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("Net Value", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("VAT", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("VAT value", HEADERS_FONT));
        table.addCell(PdfUtils.createPdfTableCell("Gross Value", HEADERS_FONT));
    }

    private void fillProductTable(PdfPTable table) {
        order.getProducts().forEach(y -> {
            table.addCell(String.valueOf(getOrdinalNumber()));
            table.addCell(String.valueOf(y.getName()));

            table.addCell(String.valueOf(y.getNumber()));
            table.addCell(String.valueOf(y.getPrice()));

            table.addCell(String.valueOf(decimalFormat.format(y.getNumber() * y.getPrice())));
            table.addCell(decimalFormat.format(customer.getVatRate() * 100) + "%");

            table.addCell(String.valueOf(decimalFormat.format(y.getNumber() * y.getPrice() * customer.getVatRate())));
            table.addCell(String.valueOf(decimalFormat.format(y.getNumber() * y.getPrice() * (1 + customer.getVatRate()))));
        });
    }

    private void fillTableSummary(PdfPTable table) {
        table.addCell(PdfUtils.emptyCell());
        table.addCell(PdfUtils.emptyCell());
        table.addCell(PdfUtils.emptyCell());
        table.addCell(PdfUtils.createBoldBorderCell("Total", BOLD_FONT));
        table.addCell(String.valueOf(order.getSubTotal()));
        table.addCell(String.valueOf(""));
        table.addCell(String.valueOf(order.getTax()));
        table.addCell(String.valueOf(order.getTotalCost()));

        table.setSpacingAfter(50);
    }

    private int getOrdinalNumber() {
        return ordinalStart.incrementAndGet();
    }

    private String createInvoiceName() {
        return String.valueOf(LocalDate.now().getYear()) + String.valueOf(LocalDate.now().getMonthValue()) +
                String.valueOf(LocalDate.now().getDayOfMonth()) + "/" + order.getId();
    }
}