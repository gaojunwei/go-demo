package com.ecust.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadSleepUtils {
    public static void sleep(Long a) {
        try {
            Thread.sleep(a * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Long getThreadId() {
        return Thread.currentThread().getId();
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    public static void log(Object logStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (logStr == null || logStr.toString().trim().equals("")) {
            System.out.println(String.format("%s [%s]", LocalDateTime.now().format(formatter),getThreadName()));
        } else {
            System.out.println(String.format("%s [%s] %s", LocalDateTime.now().format(formatter),getThreadName(), logStr));
        }
    }
}
