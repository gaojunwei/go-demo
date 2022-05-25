package com.ecust.test.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jdjr.crawler.tcpj.utils.RandomUtils;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LimitTest  {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 100L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("gjw-task-%d").build(), new ThreadPoolExecutor.AbortPolicy());

    public volatile RateLimiter limiter = RateLimiter.create(1);
    @Test
    public void test001() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100,500));
            //提交任务
            executor.execute(() -> {
                if(limiter.tryAcquire()){
                    ThreadSleepUtils.log("获取锁成功******");
                }else {
                    ThreadSleepUtils.log("获取锁失败");
                }
            });
        }

        ThreadSleepUtils.sleep(500);
        executor.shutdown();
        executor.awaitTermination(1,TimeUnit.DAYS);
    }
}
