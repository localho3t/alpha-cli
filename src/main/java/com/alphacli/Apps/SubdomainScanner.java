package com.alphacli.Apps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SubdomainScanner {
    private String path;
    private List<String> lines;
    private String domain;
    private int  threadCount;

    public SubdomainScanner(String path,String domain , int threadCount){
        this.path = path;
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            this.lines = lines;
        } catch (IOException e) {
            System.err.println("[!] Reading File Error : " + e.getMessage());
        }
        this.domain = domain;
        this.threadCount = threadCount;
    }

    public void scan(){
        System.out.println("Starting scan for domain: " + domain);
        System.out.println("Subdomains to check: " + lines.size());
    
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<SubdomainResult>> futures = new ArrayList<>();

        for (String sub : lines) {
            String fullSubdomain = sub + "." + domain;
            Callable<SubdomainResult> task = () -> {
                try {
                    InetAddress[] addresses = InetAddress.getAllByName(fullSubdomain);
                    return new SubdomainResult(fullSubdomain, addresses);
                } catch (UnknownHostException e) {
                    return new SubdomainResult(fullSubdomain, null);
                }
            };
            futures.add(executor.submit(task));
        }

        for (Future<SubdomainResult> future : futures) {
            try {
                SubdomainResult result = future.get();
                if (result.addresses != null) {
                    System.out.print(result.subdomain + " : ");
                    for (InetAddress addr : result.addresses) {
                        System.out.print(addr.getHostAddress() + " ");
                    }
                    System.out.println();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        
    }

    static class SubdomainResult {
        String subdomain;
        InetAddress[] addresses;

        SubdomainResult(String subdomain, InetAddress[] addresses) {
            this.subdomain = subdomain;
            this.addresses = addresses;
        }
    }
}
