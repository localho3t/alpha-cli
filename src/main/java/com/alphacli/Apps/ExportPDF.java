package com.alphacli.Apps;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TextAlignment;

public class ExportPDF {

    private String title;
    private SimpleDateFormat date;
    private String ip;
    private String a_records;
    private String aaaa_records;
    private String mx_records;
    private String cname_records;
    private String txt_records;
    private String ns_records;
    private String soa_records;
    private String ssl_subject;
    private String ssl_issuer;
    private String ssl_expiry_data;
    private String ssl_day_lefts;
    private List<Map<String, String>> subdomains; // لیست ساب‌دامنه‌ها

    public ExportPDF(String title) {
        this.title = title;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setSubdomains(List<Map<String, String>> subdomains) {
        this.subdomains = subdomains;
    }

    public void exportToPdf() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            String name = "src/main/resources/exports/Report_" + timestamp + ".pdf";
            
            // ایجاد دایرکتوری اگر وجود نداشته باشد
            new File("src/main/resources/exports").mkdirs();
            
            PdfWriter writer = new PdfWriter(new File(name));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // هدر گزارش
            addReportHeader(document, timestamp);
            
            // اضافه کردن لوگو
            addLogo(document);
            
            // بخش اطلاعات اصلی
            addMainInfoSection(document);
            
            // بخش ساب‌دامنه‌ها
            if (subdomains != null && !subdomains.isEmpty()) {
                addSubdomainsSection(document);
            }

            document.close();
            System.out.println("PDF report generated successfully: " + name);
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }

    private void addReportHeader(Document document, String timestamp) {
        Paragraph header = new Paragraph("Scan Report - " + title)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLUE);
        document.add(header);
        
        Paragraph subHeader = new Paragraph("Generated on: " + timestamp)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic();
        document.add(subHeader);
        
        document.add(new Paragraph("\n"));
    }

    private void addLogo(Document document) {
        try {
            String imagePath = "src/main/resources/images/logo.png";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(150, 100);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }
    }

    private void addMainInfoSection(Document document) {
        document.add(new Paragraph("Scan Results").setBold().setUnderline());
        
        Table mainTable = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                .useAllAvailableWidth()
                .setMarginTop(10);
        
        // هدر جدول
        addTableHeader(mainTable);
        
        // اضافه کردن رکوردها
        addRecordIfExists(mainTable, "IP Address", ip);
        addRecordIfExists(mainTable, "A Record", a_records);
        addRecordIfExists(mainTable, "AAAA Record", aaaa_records);
        addRecordIfExists(mainTable, "CNAME Record", cname_records);
        addRecordIfExists(mainTable, "MX Record", mx_records);
        addRecordIfExists(mainTable, "TXT Record", txt_records);
        addRecordIfExists(mainTable, "NS Record", ns_records);
        addRecordIfExists(mainTable, "SOA Record", soa_records);
        addRecordIfExists(mainTable, "SSL Subject", ssl_subject);
        addRecordIfExists(mainTable, "SSL Issuer", ssl_issuer);
        addRecordIfExists(mainTable, "SSL Expiry Date", ssl_expiry_data);
        addRecordIfExists(mainTable, "SSL Days Left", ssl_day_lefts);
        
        document.add(mainTable);
        document.add(new Paragraph("\n"));
    }

    private void addSubdomainsSection(Document document) {
        document.add(new Paragraph("Discovered Subdomains").setBold().setUnderline());
        
        Table subdomainsTable = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                .useAllAvailableWidth()
                .setMarginTop(10);
        
        // هدر جدول ساب‌دامنه‌ها
        addTableHeader(subdomainsTable);
        
        // اضافه کردن ساب‌دامنه‌ها
        // int counter = 1;
        for (Map<String, String> subdomain : subdomains) {
            String subdomainName = subdomain.get("name");
            String subdomainIp = subdomain.get("ip");
            
            // subdomainsTable.addCell(new Cell().add(new Paragraph(String.valueOf(counter++)))
                                                // .setTextAlignment(TextAlignment.CENTER));
            subdomainsTable.addCell(new Cell().add(new Paragraph(subdomainName)));
            subdomainsTable.addCell(new Cell().add(new Paragraph(subdomainIp != null ? subdomainIp : "N/A")));
        }
        
        document.add(subdomainsTable);
        
        // خلاصه ساب‌دامنه‌ها
        Paragraph summary = new Paragraph(String.format(
                "Total subdomains discovered: %d", subdomains.size()))
                .setBold()
                .setItalic();
        document.add(summary);
    }

    private void addTableHeader(Table table) {
        table.addHeaderCell(new Cell().add(new Paragraph("Record").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Value").setBold()));
    }

    private void addRecordIfExists(Table table, String title, String value) {
        if (value != null && !value.isEmpty()) {
            table.addCell(new Cell().add(new Paragraph(title).setBold()));
            table.addCell(new Cell().add(new Paragraph(value)));
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getA_records() {
        return a_records;
    }

    public void setA_records(String a_records) {
        this.a_records = a_records;
    }

    public String getAaaa_records() {
        return aaaa_records;
    }

    public void setAaaa_records(String aaaa_records) {
        this.aaaa_records = aaaa_records;
    }

    public String getMx_records() {
        return mx_records;
    }

    public void setMx_records(String mx_records) {
        this.mx_records = mx_records;
    }

    public String getCname_records() {
        return cname_records;
    }

    public void setCname_records(String cname_records) {
        this.cname_records = cname_records;
    }

    public String getTxt_records() {
        return txt_records;
    }

    public void setTxt_records(String txt_records) {
        this.txt_records = txt_records;
    }

    public String getNs_records() {
        return ns_records;
    }

    public void setNs_records(String ns_records) {
        this.ns_records = ns_records;
    }

    public String getSoa_records() {
        return soa_records;
    }

    public void setSoa_records(String soa_records) {
        this.soa_records = soa_records;
    }

    public String getSsl_subject() {
        return ssl_subject;
    }

    public void setSsl_subject(String ssl_subject) {
        this.ssl_subject = ssl_subject;
    }

    public String getSsl_issuer() {
        return ssl_issuer;
    }

    public void setSsl_issuer(String ssl_issuer) {
        this.ssl_issuer = ssl_issuer;
    }

    public String getSsl_expiry_data() {
        return ssl_expiry_data;
    }

    public void setSsl_expiry_data(String ssl_expiry_data) {
        this.ssl_expiry_data = ssl_expiry_data;
    }

    public String getSsl_day_lefts() {
        return ssl_day_lefts;
    }

    public void setSsl_day_lefts(String ssl_day_lefts) {
        this.ssl_day_lefts = ssl_day_lefts;
    }
    

}
