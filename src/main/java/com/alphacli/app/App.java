package com.alphacli.app;




import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.alphacli.Apps.AdvancedDNSLookup;
import com.alphacli.Apps.Domain;
import com.alphacli.Apps.ExportPDF;
import com.alphacli.Apps.SSLChecker;
import com.alphacli.Apps.SubdomainScanner;
import com.alphacli.Apps.SubdomainScanner.SubdomainResult;
import com.alphacli.Argv.Arguments;
import com.alphacli.Utils.Utils;


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
        Utils utils = new Utils();
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
            ExportPDF doc = new ExportPDF("Scan Report for "+domain_app.getDomain());


            AdvancedDNSLookup adv = new AdvancedDNSLookup(domain_app.getDomain());
            SSLChecker sc = new SSLChecker(domain_app.getDomain(),443);

            


            if (cmd.hasOption("ip")) {
                System.out.println("[*] IP addresses for " + domain_app.getDomain() + ":");
                for (InetAddress address : domain_app.getAddresses()) {
                    System.out.println("  - " + address.getHostAddress());
                }
                doc.setIp(utils.InetAddressToString(domain_app.getAddresses()));
            }
            
            if (cmd.hasOption("A")) {
                System.out.println("[*] A Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getARecords()));
                doc.setA_records(utils.StringArraysToString(adv.getARecords()));
            }

            if (cmd.hasOption("MX")) {
                System.out.println("[*] MX Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getMXRecords()));
                doc.setMx_records(utils.StringArraysToString(adv.getMXRecords()));

            }

            if (cmd.hasOption("AAAA")) {
                System.out.println("[*] AAAA Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getAAAARecords()));
                doc.setAaaa_records(utils.StringArraysToString(adv.getAAAARecords()));

            }

            if (cmd.hasOption("CNAME")) {
                System.out.println("[*] CNAME Record " + domain_app.getDomain() + ":");
                System.out.println(adv.getCNAMERecord());
                doc.setCname_records(adv.getCNAMERecord());
            }

            if (cmd.hasOption("NS")) {
                System.out.println("[*] NS Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getNSRecords()));
                doc.setNs_records(utils.StringArraysToString(adv.getNSRecords()));
            }

            if (cmd.hasOption("SOA")) {
                System.out.println("[*] SOA Record " + domain_app.getDomain() + ":");
                System.out.println(adv.getSOARecord());
                doc.setSoa_records(adv.getSOARecord());
            }

            if (cmd.hasOption("TXT")) {
                System.out.println("[*] TXT Record " + domain_app.getDomain() + ":");
                System.out.println(Arrays.toString(adv.getTXTRecords()));
                doc.setTxt_records(utils.StringArraysToString(adv.getTXTRecords()));
            }


            if (cmd.hasOption("sslc")) {
                System.out.println("[*] Check SSL : " + domain_app.getDomain() + ":");
                sc.checkSSL();
                doc.setSsl_subject(sc.get_subject());
                doc.setSsl_issuer(sc.get_issuer());
                doc.setSsl_expiry_data(sc.get_expiryDate());
                doc.setSsl_day_lefts(sc.get_daysLeft());
            }

            
            if (cmd.hasOption("subscan")) {
                if (cmd.hasOption("subpath")) {
                    System.out.println("[*] scanning ...");
                    // SubdomainScanner ss = new SubdomainScanner(cmd.getOptionValue("subpath"), domain_app.getDomain(), 2);
                    // SubdomainScanner scanner = new SubdomainScanner(cmd.getOptionValue("subpath"), domain_app.getDomain(), 2);
                    SubdomainScanner scanner = new SubdomainScanner(cmd.getOptionValue("subpath"), domain_app.getDomain(), 12);
                    List<SubdomainResult> results = scanner.scan();
                    List<Map<String, String>> exportData = new ArrayList<>();
                    for (SubdomainResult result : results) {
                        Map<String, String> entry = new HashMap<>();
                        entry.put("name", result.getSubdomain());
                        
                        StringBuilder ips = new StringBuilder();
                        for (InetAddress addr : result.getAddresses()) {
                            ips.append(addr.getHostAddress()).append(", ");
                        }
                        entry.put("ip", ips.length() > 0 ? ips.substring(0, ips.length()-2) : "");
                        
                        exportData.add(entry);
                        doc.setSubdomains(exportData);
                    }
                }else{
                    System.out.println("[*] subdomain wordlist path : not found!");
                }
            }

            if (cmd.hasOption("epdf")) {
                System.out.println("[*] Exporting ...");
                doc.exportToPdf();
            }

        } catch (ParseException e) {
            System.err.println("arguments exceptions : " + e.getMessage());
            formatter.printHelp("Alpha CLI ", options);
            System.exit(1);
        }
    }

}
