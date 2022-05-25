package com.ecust.test;

import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        ThreadSleepUtils.log("*******任务数溢出当前队列数：" + executor.getCompletedTaskCount());
    }
}
