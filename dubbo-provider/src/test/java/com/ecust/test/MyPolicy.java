package com.ecust.test;

import com.gjw.deme.utils.ThreadSleepUtils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        ThreadSleepUtils.log("*******任务数溢出当前队列数：" + executor.getCompletedTaskCount());
    }
}
