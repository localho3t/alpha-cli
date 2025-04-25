package com.alphacli.app;




import java.net.InetAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.alphacli.Apps.Domain;
import com.alphacli.Argv.Arguments;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Arguments arg = new Arguments();
        Options options = arg.arguments();

        Domain domain_app = new Domain();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            
            
            if (cmd.hasOption("help")) {
                formatter.printHelp("Alpha CLI", options);
                System.exit(0);
            }
            
            if (cmd.hasOption("d") || cmd.hasOption("domain") ){
                domain_app.setDomain(cmd.getOptionValue("domain"));
                System.out.println("[*] set domain : "+domain_app.getDomain());
            }

            if (cmd.hasOption("ip")) {
                System.out.println("[*] IP addresses for " + domain_app.getDomain() + ":");
                for (InetAddress address : domain_app.getAddresses()) {
                    System.out.println("  - " + address.getHostAddress());
                }
            }
            
            
        } catch (ParseException e) {
            System.err.println("arguments exceptions : " + e.getMessage());
            formatter.printHelp("Alpha CLI ", options);
            System.exit(1);
        }
    }

}
