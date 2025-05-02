package com.alphacli.Apps;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class ExportPDF {

    private String title ;
    private SimpleDateFormat date;
    private String ip ;
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

    public ExportPDF(String title){
        this.title = title;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    
    public void Export(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            String name = "src/main/resources/exports/SSL_Report_" + timestamp + ".pdf";
            PdfWriter writer = new PdfWriter(new File(name));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("PDF Export | "+title+" | "+timestamp)
                    .setFontSize(16)
                    .setBold());
            

            try {
                
                String image_path = "src/main/resources/images/logo.png";

                ImageData imageData = ImageDataFactory.create(image_path);
                Image image = new Image(imageData);
                
                image.scaleToFit(300, 200);
                
                image.setHorizontalAlignment(HorizontalAlignment.CENTER);
                
                document.add(image);
                document.add(new Paragraph("\n")); 
                
            } catch (MalformedURLException e) {
                System.err.println("Image Not Found: " + e.getMessage());
            }
            
            
            document.add(new Paragraph("Data :").setBold());
            
            
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 2}))
                    .useAllAvailableWidth();
            int rowNumber = 1;
            
            
            table.addHeaderCell(new Cell().add(new Paragraph("Record").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Title").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Value").setBold()));
            
            if (a_records != null && !a_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("A Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(a_records)));
            }

            
            if (aaaa_records != null && !aaaa_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("AAAA Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(aaaa_records)));
            }

            
            if (cname_records != null && !cname_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("CNAME Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(cname_records)));
            }

            
            if (mx_records != null && !mx_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("MX Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(mx_records)));
            }

            
            if (txt_records != null && !txt_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("TXT Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(txt_records)));
            }

            
            if (ns_records != null && !ns_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("NS Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(ns_records)));
            }

            
            if (soa_records != null && !soa_records.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("SOA Record").setBold()));
                table.addCell(new Cell().add(new Paragraph(soa_records)));
            }

            
            if (ssl_subject != null && !ssl_subject.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("SSL Subject").setBold()));
                table.addCell(new Cell().add(new Paragraph(ssl_subject)));
            }

            if (ssl_issuer != null && !ssl_issuer.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("SSL Issuer").setBold()));
                table.addCell(new Cell().add(new Paragraph(ssl_issuer)));
            }

            if (ssl_expiry_data != null && !ssl_expiry_data.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("SSL Expiry Date").setBold()));
                table.addCell(new Cell().add(new Paragraph(ssl_expiry_data)));
            }

            if (ssl_day_lefts != null && !ssl_day_lefts.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNumber++))));
                table.addCell(new Cell().add(new Paragraph("SSL Days Left").setBold()));
                table.addCell(new Cell().add(new Paragraph(ssl_day_lefts)));
            }

            document.add(table);
        
            document.close();
        } catch (IOException e) {
            
            System.out.println(e);
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
