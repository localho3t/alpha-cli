package com.alphacli.Apps;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Domain {
    
    private String domain;
    private InetAddress[] addresses; 
    private static final String DOMAIN_PATTERN = 
        "^(https?:\\/\\/)?(x\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";
    private static final Pattern pattern = Pattern.compile(DOMAIN_PATTERN);

// getter 
    public String getDomain(){
        return this.domain;
    }
    public InetAddress[] getAddresses() {
        return addresses != null ? addresses : new InetAddress[0];
    }
// setter
    // private void setAddresses(InetAddress[] addresses) {
    //     this.addresses = addresses;
    // }

    public void setDomain(String domain){
        
        if (isValidDomain(domain)) {
            try {
                this.domain = domain;
                this.addresses = InetAddress.getAllByName(domain);
                System.out.println("[*] Successfully resolved IPs for domain: " + domain);
            } catch (UnknownHostException e) {
                System.err.println("Domain Exception: '" + domain + "'");
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.err.println("[!] Invalid domain format: " + domain);
            this.addresses = new InetAddress[0];
            System.exit(1);
        }
    }


// other functions
    private static boolean isValidDomain(String domain) {
        Matcher matcher = pattern.matcher(domain);
        return matcher.matches();
    }
}