package com.jdjr.crawler.tcpj.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 主要功能描述：并发量大的情况下获取时间戳，有一定精度问题
 *
 * @author gaojunwei
 * @date 2021/6/3
 */
public class SystemClockUtils {
    private static final SystemClockUtils MILLIS_CLOCK = new SystemClockUtils(1);
    private final long precision;
    private final AtomicLong now;

    private SystemClockUtils(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    public static SystemClockUtils millisClock() {
        return MILLIS_CLOCK;
    }

    /**
     * 创建定时守护线程任务，维护缓存中的时间戳
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "system.clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取缓存中当前时间戳
     */
    public long now() {
        return now.get();
    }
}
