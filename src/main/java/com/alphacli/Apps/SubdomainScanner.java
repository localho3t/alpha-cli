package com.alphacli.Apps;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class SubdomainScanner {
    private final String domain;
    private final List<String> lines;
    private final int threadCount;
    
    public SubdomainScanner(String path, String domain, int threadCount) {
        this.domain = domain;
        this.threadCount = threadCount;
        this.lines = readSubdomainsFromFile(path);
    }
    
    private List<String> readSubdomainsFromFile(String path) {
        List<String> subdomains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) { // خطوط خالی و کامنت‌ها را نادیده بگیر
                    subdomains.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("[!] Error reading subdomains file: " + e.getMessage());
            System.exit(1);
        }
        
        if (subdomains.isEmpty()) {
            System.err.println("[!] No valid subdomains found in the file");
            System.exit(1);
        }
        
        return subdomains;
    }
    
    public List<SubdomainResult> scan() {
        System.out.printf("[*] Starting scan for %s with %d threads\n", domain, threadCount);
        System.out.printf("[*] Loaded %d subdomains from file\n", lines.size());
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<SubdomainResult>> futures = new ArrayList<>();
        List<SubdomainResult> results = new ArrayList<>();
        
        // Submit all scanning tasks
        for (String sub : lines) {
            String fullSubdomain = sub + "." + domain;
            futures.add(executor.submit(() -> scanSubdomain(fullSubdomain)));
        }
        
        // Process results
        int foundCount = 0;
        for (Future<SubdomainResult> future : futures) {
            try {
                SubdomainResult result = future.get();
                if (result.isResolved()) {
                    results.add(result);
                    foundCount++;
                    printSubdomainResult(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("[!] Error during scanning: " + e.getMessage());
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
        
        System.out.printf("\n[*] Scan completed. Found %d active subdomains out of %d\n", 
                        foundCount, lines.size());
        
        return results;
    }
    
    private SubdomainResult scanSubdomain(String fullSubdomain) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(fullSubdomain);
            return new SubdomainResult(fullSubdomain, addresses);
        } catch (UnknownHostException e) {
            return new SubdomainResult(fullSubdomain, null);
        }
    }
    
    private void printSubdomainResult(SubdomainResult result) {
        System.out.printf("[+] %-40s : ", result.getSubdomain());
        for (InetAddress addr : result.getAddresses()) {
            System.out.print(addr.getHostAddress() + " ");
        }
        System.out.println();
    }
    
    public static class SubdomainResult {
        private final String subdomain;
        private final InetAddress[] addresses;
        
        public SubdomainResult(String subdomain, InetAddress[] addresses) {
            this.subdomain = subdomain;
            this.addresses = addresses;
        }
        
        public String getSubdomain() { return subdomain; }
        public InetAddress[] getAddresses() { return addresses; }
        public boolean isResolved() { return addresses != null && addresses.length > 0; }
    }
}