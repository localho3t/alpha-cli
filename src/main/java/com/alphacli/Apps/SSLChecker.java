package com.alphacli.Apps;

import javax.net.ssl.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;



public class SSLChecker {
    private int port;
    private String host;

    private String subject;
    private String issuer ;
    private String expiryDate;
    private String daysLeft;

    public SSLChecker(String host,int port){
        this.host = host;
        this.port = port;
    }
    public void checkSSL(){
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,null,null);

            // ssl socket
            SSLSocketFactory factory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket(host,port);
            socket.startHandshake();

            // get certificate
            Certificate[] certificates = socket.getSession().getPeerCertificates();
            X509Certificate x509Certificate = (X509Certificate) certificates[0];
            String subject = x509Certificate.getSubjectX500Principal().getName();
            this.subject = subject; 
            String issuer = x509Certificate.getIssuerX500Principal().getName();
            this.issuer = issuer;
            
            Date expiryDate = x509Certificate.getNotAfter();
            this.expiryDate = formatDate(expiryDate);

            long daysLeft = ChronoUnit.DAYS.between(
                LocalDate.now(),
                expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            this.daysLeft = String.valueOf(daysLeft);

            System.out.println("--- SSL Certificate Info ---");
            System.out.println("Subject: " + this.subject);
            System.out.println("Issuer: " + this.issuer);
            System.out.println("Expiry Date: " + this.expiryDate);
            System.out.println("Days Left: " + this.daysLeft);

        } catch (Exception e) {
            System.err.println("Error checking SSL: " + e.getMessage());
        }

        
    }
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    public String get_subject(){
        return this.subject;
    }
    public String get_issuer(){
        return this.issuer;
    }
    public String get_expiryDate(){
        return this.expiryDate;
    }
    public String get_daysLeft(){
        return this.daysLeft;
    }
}
