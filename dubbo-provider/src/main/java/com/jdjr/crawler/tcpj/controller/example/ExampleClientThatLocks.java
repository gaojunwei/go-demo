package com.jdjr.crawler.tcpj.controller.example;

import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ExampleClientThatLocks {
    private final InterProcessSemaphoreMutex lock;
    private final FakeLimitedResource resource;
    private final String clientName;

    public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessSemaphoreMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            logger.info("获取锁失败 {}", this.clientName);
            return;
        }
        if (!lock.acquire(time, unit)) {
            logger.info("获取锁失败 {}", this.clientName);
            return;
        }
        try {
            logger.info("{} 成功获取锁", clientName);
            ThreadSleepUtils.sleep(1);
            resource.use();
        } finally {
            logger.info("{} 释放锁", clientName);
            lock.release();
        }
    }
}
