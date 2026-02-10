package bg.autosalon.utils;

import bg.autosalon.entities.Sale;
import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.services.SaleService;
import bg.autosalon.services.ServiceRecordService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class PdfReportGenerator {

    private final SaleService saleService = new SaleService();
    private final ServiceRecordService serviceService = new ServiceRecordService();

    public void generateFinancialReport(File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));

        document.open();


        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Financial Report - Autosalon", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph date = new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20);
        document.add(date);


        document.add(new Paragraph("Car Sales Revenue", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph(" "));

        PdfPTable salesTable = new PdfPTable(4);
        salesTable.setWidthPercentage(100);
        addTableHeader(salesTable, "Car", "Client", "Date", "Price (BGN)");

        List<Sale> sales = saleService.getAllSales();
        double totalSales = 0;

        for (Sale sale : sales) {
            addCell(salesTable, sale.getCar().getBrand() + " " + sale.getCar().getModel());
            addCell(salesTable, sale.getClient().getFirstName() + " " + sale.getClient().getLastName());
            addCell(salesTable, sale.getSaleDate().toString());
            addCell(salesTable, String.format("%.2f", sale.getFinalPrice()));
            totalSales += sale.getFinalPrice();
        }
        document.add(salesTable);


        Paragraph salesTotal = new Paragraph("Total Sales: " + String.format("%.2f BGN", totalSales));
        salesTotal.setAlignment(Element.ALIGN_RIGHT);
        salesTotal.setSpacingAfter(15);
        document.add(salesTotal);


        document.add(new Paragraph("Service & Repairs Revenue", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph(" "));

        PdfPTable serviceTable = new PdfPTable(4);
        serviceTable.setWidthPercentage(100);
        addTableHeader(serviceTable, "Car", "Type", "Date", "Cost (BGN)");

        List<ServiceRecord> records = serviceService.getAllRecords();
        double totalService = 0;

        for (ServiceRecord rec : records) {
            addCell(serviceTable, rec.getCar().getBrand() + " " + rec.getCar().getModel());
            addCell(serviceTable, rec.getType().toString());
            addCell(serviceTable, rec.getDate().toString());
            addCell(serviceTable, String.format("%.2f", rec.getPrice()));
            totalService += rec.getPrice();
        }
        document.add(serviceTable);


        Paragraph serviceTotalPara = new Paragraph("Total Service: " + String.format("%.2f BGN", totalService));
        serviceTotalPara.setAlignment(Element.ALIGN_RIGHT);
        serviceTotalPara.setSpacingAfter(20);
        document.add(serviceTotalPara);


        Font grandTotalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph grandTotal = new Paragraph("TOTAL REVENUE: " + String.format("%.2f BGN", totalSales + totalService), grandTotalFont);
        grandTotal.setAlignment(Element.ALIGN_RIGHT);
        document.add(grandTotal);

        document.close();
    }


    private void addTableHeader(PdfPTable table, String... headers) {
        Stream.of(headers).forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }


    private void addCell(PdfPTable table, String text) {
        table.addCell(text != null ? text : "");
    }
}