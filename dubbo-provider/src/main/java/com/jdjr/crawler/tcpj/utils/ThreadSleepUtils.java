package com.jdjr.crawler.tcpj.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 类描述
 **/
@Slf4j
public class ThreadSleepUtils {

    /**
     * 休眠-单位:秒
     */
    public static void sleep(Integer second) {
        for (int i = 1; i <= second.intValue(); i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 休眠-单位:毫秒
     */
    public static void sleepMS(Integer millisecond) {
        try {
            logger.info("do sleep {}ms", millisecond);
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage(), e);
        }
    }

    public static String ThreadName() {
        return Thread.currentThread().getName();
    }

    public static long ThreadId() {
        return Thread.currentThread().getId();
    }
}
