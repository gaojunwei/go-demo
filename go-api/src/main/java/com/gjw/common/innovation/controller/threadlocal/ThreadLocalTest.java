package com.gjw.common.innovation.controller.threadlocal;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadLocalTest {
    public void test002() throws InterruptedException {
        logger.info("测试开始");
        System.out.println();
        doWork();
        System.out.println();
        logger.info("测试结束");
    }

    public void doWork() throws InterruptedException {
        //线程池
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutor();
        //设置父线程传递变量
        MyThreadLocal.inheritableThreadLocal.set("父线程值-1");

        for (int i = 0; i < 3; i++) {
            MdcRunnable mdcRunnable = new MdcRunnable(new Task());

            threadPoolExecutor.execute(mdcRunnable);
        }

        TimeUnit.SECONDS.sleep(5);
        logger.info("测试程序结束 父线程传播值：{}，线程变量值：{}", MyThreadLocal.inheritableThreadLocal.get(), MyThreadLocal.threadLocalCmdData.get());
    }

    private ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(200, 200,
                120L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),//无界，FIFO队列
                new ThreadFactoryBuilder().setNameFormat("exe-cmd-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
        //注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdown();
        }));
        return threadPool;
    }
}
