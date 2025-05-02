package com.alphacli.app;




import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.alphacli.Apps.AdvancedDNSLookup;
import com.alphacli.Apps.Domain;
import com.alphacli.Apps.SSLChecker;
import com.alphacli.Argv.Arguments;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
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

            AdvancedDNSLookup adv = new AdvancedDNSLookup(domain_app.getDomain());
            SSLChecker sc = new SSLChecker(domain_app.getDomain(),443);

            


            if (cmd.hasOption("ip")) {
                System.out.println("[*] IP addresses for " + domain_app.getDomain() + ":");
                for (InetAddress address : domain_app.getAddresses()) {
                    System.out.println("  - " + address.getHostAddress());
                }
            }
            
            if (cmd.hasOption("A")) {
                System.out.println("[*] A Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getARecords()));
            }

            if (cmd.hasOption("MX")) {
                System.out.println("[*] MX Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getMXRecords()));
            }

            if (cmd.hasOption("AAAA")) {
                System.out.println("[*] AAAA Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getAAAARecords()));
            }

            if (cmd.hasOption("CNAME")) {
                System.out.println("[*] CNAME Record " + domain_app.getDomain() + ":");
                System.out.println(adv.getCNAMERecord());
            }

            if (cmd.hasOption("NS")) {
                System.out.println("[*] NS Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getNSRecords()));
            }

            if (cmd.hasOption("SOA")) {
                System.out.println("[*] SOA Record " + domain_app.getDomain() + ":");
                System.out.println(adv.getSOARecord());
            }

            if (cmd.hasOption("TXT")) {
                System.out.println("[*] SOA Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getTXTRecords()));
            }
            if (cmd.hasOption("rall")) {
                System.out.println("[*] ALL Record " + domain_app.getDomain() + ":");
                adv.runFullScan();
            }

            if (cmd.hasOption("sslc")) {
                System.out.println("[*] Check SSL : " + domain_app.getDomain() + ":");
                sc.checkSSL();
            }
            
        } catch (ParseException e) {
            System.err.println("arguments exceptions : " + e.getMessage());
            formatter.printHelp("Alpha CLI ", options);
            System.exit(1);
        }
    }

}
