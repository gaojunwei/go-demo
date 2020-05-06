package com.gjw.common.utils;

public class IpUtils {
    public static Long ip2long(String ip) {
        String[] sctions = ip.split("\\.");
        return (Long.parseLong(sctions[0]) << 24) + (Long.parseLong(sctions[1]) << 16) + (Long.parseLong(sctions[2]) << 8) + (Long.parseLong(sctions[3]));
    }

    public static String long2ip(Long ip) {
        StringBuilder str = new StringBuilder();
        //直接右移24位
        str.append(ip >>> 24).append(".");
        //将高8位置0，然后无符号右移16位
        str.append((ip & 0x00FFFFFF) >>> 16).append(".");
        //将高16位置0，然后无符号右移8位
        str.append((ip & 0x0000FFFF) >>> 8).append(".");
        //将高24位置0
        str.append(ip & 0x000000FF);
        return str.toString();
    }

    public static void main(String[] args) {
        String ip = "255.255.255.255";
        long a = IpUtils.ip2long(ip);
        System.out.println(a);
        String ipStr = IpUtils.long2ip(a);
        System.out.println(ipStr);
    }
}
