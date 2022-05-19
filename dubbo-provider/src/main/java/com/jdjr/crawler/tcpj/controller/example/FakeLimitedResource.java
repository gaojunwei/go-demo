package com.jdjr.crawler.tcpj.controller.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 共享资源
 */
@Slf4j
public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    public void use() throws InterruptedException {
        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }
        logger.info("修改成功");
        try {
            //Thread.sleep((long) (3 * Math.random()));
        } finally {
            //inUse.set(false);
        }
    }
}
