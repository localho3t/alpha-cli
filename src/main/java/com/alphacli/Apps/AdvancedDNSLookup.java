package com.alphacli.Apps;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

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
    public String[] getMXRecords(){
        try {
            Record[] records = new Lookup(domain,Type.MX).run();
            return Arrays.stream(records)
                .map(record -> ((MXRecord) record).getTarget().toString())
                .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }
    public  String getCNAMERecord() {
        try {
            Record[] records = new Lookup(domain, Type.CNAME).run();
            return records != null ? ((CNAMERecord) records[0]).getTarget().toString() : "Not Found";
        } catch (Exception e) {
            return "Not Found";
        }
    }

    // Get TXT Records
    public String[] getTXTRecords() {
        try {
            Record[] records = new Lookup(domain, Type.TXT).run();
            return Arrays.stream(records)
                    .map(record -> ((TXTRecord) record).getStrings().get(0))
                    .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }

    // Get Ns Records
    public String[] getNSRecords() {
        try {
            Record[] records = new Lookup(domain, Type.NS).run();
            return Arrays.stream(records)
                    .map(record -> ((NSRecord) record).getTarget().toString())
                    .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }

    // get SOA Records
    public String getSOARecord() {
        try {
            Record[] records = new Lookup(domain, Type.SOA).run();
            return records != null ? records[0].toString() : "Not Found";
        } catch (Exception e) {
            return "Not Found";
        }
    }

    // get ALL Records
    public void runFullScan() {
        Map<String, Supplier<Object>> scanModules = new LinkedHashMap<>();
        
        scanModules.put("A Records", this::getARecords);
        scanModules.put("AAAA Records", this::getAAAARecords);
        scanModules.put("MX Records", this::getMXRecords);
        scanModules.put("NS Records", this::getNSRecords);
        scanModules.put("TXT Records", this::getTXTRecords);
        scanModules.put("CNAME Record", this::getCNAMERecord);
        scanModules.put("SOA Record", this::getSOARecord);
    
        System.out.println("\n=== DNS Scan Results for " + domain + " ===");
        
        scanModules.forEach((name, task) -> {
            System.out.println("\n[*] " + name + ":");
            System.out.println(task.get());
        });
        
        System.out.println("\n=== Scan completed ===");
    }
    
}
