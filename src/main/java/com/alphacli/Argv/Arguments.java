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


        return options;
    }
}
