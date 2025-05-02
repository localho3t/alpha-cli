package com.alphacli.Utils;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {
    public String StringArraysToString(String[] data){
        return String.join(" - ", data);
    }
    public String InetAddressToString(InetAddress[] data){
        if (data == null) {
            return "";
        }
        
        return Arrays.stream(data)
                .filter(Objects::nonNull)
                .map(InetAddress::getHostAddress)
                .collect(Collectors.joining(", "));
    }
}
