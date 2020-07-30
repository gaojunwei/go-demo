package com.jdjr.crawler.tcpj.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/29 20:24
 **/
@Slf4j
public class ThreadSleepUtils {

    /**
     * 休眠-单位:秒
     */
    public static void sleep(Integer second) {
        for (int i = 1; i <= second.intValue(); i++) {
            try {
                logger.info("do sleep {}/{}s", i, second);
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
}
