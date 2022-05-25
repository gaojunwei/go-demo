package com.ecust.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainOne {
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 100L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("gjw-task-%d").build(),new MyPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            executor.execute(()->task());
            //ThreadSleepUtils.sleep(1L);
        }

        ThreadSleepUtils.sleep(1000);
    }

    public static void task() {
        ThreadSleepUtils.log("开始任务 "+executor.getPoolSize() +" "+executor.getCorePoolSize());
        //ThreadSleepUtils.sleep(1L);
        //ThreadSleepUtils.log("任务 end");
    }
}
