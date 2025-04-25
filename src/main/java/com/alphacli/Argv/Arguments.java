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

        return options;
    }
}
