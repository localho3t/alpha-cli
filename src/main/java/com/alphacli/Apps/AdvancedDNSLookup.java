package com.alphacli.Apps;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;


import java.util.Arrays;

public class AdvancedDNSLookup {
    // Get A Records
    public String[] getARecords(String domain){
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
    public String[] getAAAARecords(String domain){
        try {
            Record[] records = new Lookup(domain,Type.AAAA).run();
            return Arrays.stream(records)
                .map(record -> ((AAAARecord) record).getAddress().getHostAddress())
                .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Not Found"};
        }
    }
}
