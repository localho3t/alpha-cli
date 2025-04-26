package com.alphacli.Apps;

import javax.net.ssl.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;



public class SSLChecker {
    private int port;
    private String host;
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
            System.out.println("--- SSL/TLS Certificate Info ---");
            System.out.println("Subject: " + x509Certificate.getSubjectDN());
            System.out.println("Issuer: " + x509Certificate.getIssuerDN());
            System.out.println("Expiry Date: " + x509Certificate.getNotAfter());
            System.out.println("Days Left: " + 
                ((x509Certificate.getNotAfter().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24)));
            socket.close();

        } catch (Exception e) {
            System.err.println("Error checking SSL: " + e.getMessage());
        }
    }
}
