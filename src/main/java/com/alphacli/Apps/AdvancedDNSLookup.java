package com.alphacli.Apps;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;


import java.util.Arrays;

public class AdvancedDNSLookup {
    private String domain ;
    
    public AdvancedDNSLookup(String domain){
        this.domain = domain;
    }
    // Get A Records
    public String[] getARecords(){
        try {
            Record[] records = new Lookup(domain,Type.A).run();
            return Arrays.stream(records)
                .map(record -> ((ARecord) record).getAddress().getHostAddress())
                .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }

    // get AAAA Records
    public String[] getAAAARecords(){
        try {
            Record[] records = new Lookup(domain,Type.AAAA).run();
            return Arrays.stream(records)
                .map(record -> ((AAAARecord) record).getAddress().getHostAddress())
                .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }

    // get MX Record
    // public String[] getMXRescords()
}
