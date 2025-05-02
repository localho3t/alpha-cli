package com.alphacli.Argv;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Arguments {
    public Options arguments(){
        Options options = new Options();
        Option help = new Option("h", "help",false,"show help");
        options.addOption(help);
    
        Option input = Option.builder("d")
        .longOpt("domain")
        .argName("domain")
        .hasArg()
        .required()
        .desc("Target Domain")
        .build();
        options.addOption(input);

        Option ip = Option.builder("ip")
        .argName("ip")
        .required()
        .desc("Target Ips")
        .build();
        options.addOption(ip);

        Option a_record = Option.builder("A")
        .argName("A")
        .desc("Target A Record")
        .build();
        options.addOption(a_record);

        Option aaaa_record = Option.builder("AAAA")
        .argName("AAAA")
        .desc("Target AAAA Record")
        .build();
        options.addOption(aaaa_record);

        Option mx_record = Option.builder("MX")
        .argName("MX")
        .desc("Target MX Record")
        .build();
        options.addOption(mx_record);

        Option cname_record = Option.builder("CNAME")
        .argName("CNAME")
        .desc("Target CNAME Record")
        .build();
        options.addOption(cname_record);

        Option ns_record = Option.builder("NS")
        .argName("NS")
        .desc("Target NS Record")
        .build();
        options.addOption(ns_record);

        Option soa_record = Option.builder("SOA")
        .argName("SOA")
        .desc("Target SOA Record")
        .build();
        options.addOption(soa_record);

        Option txt_record = Option.builder("TXT")
        .argName("TXT")
        .desc("Target TXT Record")
        .build();
        options.addOption(txt_record);

        Option all_records = Option.builder("rall")
        .longOpt("record-all")
        .argName("RAll")
        .desc("Target ALL Records")
        .build();
        options.addOption(all_records);

        Option ssl_checker = Option.builder("sslc")
        .longOpt("ssl-checker")
        .argName("SCh")
        .desc("Check Target ssl")
        .build();
        options.addOption(ssl_checker);

        Option pdf_export = Option.builder("epdf")
        .longOpt("export-pdf")
        .argName("epdf")
        .desc("export pdf")
        .build();
        options.addOption(pdf_export);


        return options;
    }
}
