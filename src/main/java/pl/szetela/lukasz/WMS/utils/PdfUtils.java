package pl.szetela.lukasz.WMS.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class PdfUtils {

    public static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.TIMES, 12, Font.NORMAL);

    public static Paragraph createRowParagraph(String fieldName, String value, Font font) {
        return createRowParagraph(fieldName, value, font, 0);
    }

    public static Paragraph createRowParagraph(String fieldName, String value, Font font, int spacingBefore) {
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(spacingBefore);
        Phrase invoiceNumberPhrase = new Phrase(fieldName + ": ", font);
        paragraph.add(invoiceNumberPhrase);
        paragraph.add(value);
        return paragraph;
    }

    public static PdfPCell emptyCell() {
        PdfPCell empty = new PdfPCell();
        empty.setBorder(Rectangle.NO_BORDER);
        empty.setBackgroundColor(BaseColor.WHITE);
        return empty;
    }

    public static PdfPCell createWithoutBorderCell(String value) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPhrase(new Phrase(value));
        return cell;
    }

    public static PdfPCell createBoldWithoutBorderCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public static PdfPCell createBoldBorderCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public static PdfPCell createPdfTableCell(String columnTitle, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);
        cell.setPhrase(new Phrase(columnTitle, font));
        return cell;
    }

    public static Paragraph createRightAlignLine(String value, Font font, int spacingBefore) {
        Paragraph line = new Paragraph();
        line.setSpacingBefore(spacingBefore);
        line.add(new Chunk(new VerticalPositionMark()));
        line.add(new Phrase(value, font));
        return line;
    }

    public static Paragraph createRightAlignLine(String value, Font font) {
        return createRightAlignLine(value, font, 0);
    }

    public static Paragraph createRightAlignLine(String value) {
        return createRightAlignLine(value, NORMAL_FONT, 0);
    }

    public static Paragraph createSpaceLine() {
        Paragraph space = new Paragraph();
        LineSeparator line = new LineSeparator();
        line.setOffset(-2);
        line.setLineColor(BaseColor.GRAY);
        space.add(line);
        return space;
    }
}
