/*
package com.test.delay;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import java.util.concurrent.*;

public class DelayQueueExample {
    static DelayQueue<DelayElement> delayQueue = new DelayQueue<>();

    static ExecutorService executorService = new ThreadPoolExecutor(200, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("gjw-work-%d").build());

    @Test
    public void mainTest() throws InterruptedException {
        //添加延时消息
        addData();
        //消费消息
        new Thread(new ConsumerTask()).start();
        System.out.println("消费者任务启动");
        TimeUnit.SECONDS.sleep(100L);
    }

    private void addData() {
        for (int i = 0; i < 1000; i++) {
            delayQueue.add(new DelayElement(System.currentTimeMillis(), 1000+i));
        }
    }


    */
/**
     * 消费数据
     *//*

    class ConsumerTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    executorService.execute(delayQueue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class DelayElement implements Delayed, Runnable {
        private final long expireTime;
        private final Long e;
        private Long delayT;

        public DelayElement(Long e, long delay) {
            this.expireTime = System.currentTimeMillis() + delay;
            this.e = e;
            this.delayT = delay;
        }

        */
/**
         * 这个方法是消息是否到期（是否可以被读取出来）判断的依据
         *//*

        @Override
        public long getDelay(TimeUnit unit) {
            //判断是否过期
            return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        */
/**
         * 负整数、零或正整数，因为此对象小于、等于或大于指定对象
         *//*

        @Override
        public int compareTo(Delayed delayed) {
            //排序规则：按照过期时间排序
            DelayElement that = (DelayElement) delayed;
            if (this.expireTime < that.expireTime) {
                return -1;
            } else if (this.expireTime > that.expireTime) {
                return 1;
            } else {
                return 0;
            }
        }

        public Long getData() {
            return e;
        }

        @Override
        public void run() {
            long delayTime = System.currentTimeMillis() - getData();
            System.out.println("开始工作 延迟时间 " + delayTime + "ms 误差 " + (delayTime - this.delayT));
        }
    }
}
*/
