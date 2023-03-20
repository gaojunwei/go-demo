package com.gjw.common.innovation.controller.threadlocal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task implements Runnable{
    @Override
    public void run() {
        logger.info("！！！开始任 父线程传播值：{}，线程变量值：{}",MyThreadLocal.inheritableThreadLocal.get(),MyThreadLocal.threadLocalCmdData.get());
        logger.info("！！！任务结束");
    }
}
