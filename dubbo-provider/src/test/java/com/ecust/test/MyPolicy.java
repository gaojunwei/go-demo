package com.ecust.test;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        ThreadSleepUtils.log("当前队列数：" + executor.getCompletedTaskCount());
    }
}
