package com.ecust.test.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jdjr.crawler.tcpj.utils.RandomUtils;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainThreadTest {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 100L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("gjw-task-%d").build(), new ThreadPoolExecutor.AbortPolicy());

    @Before
    public void before() {
        System.out.println("-----程序开始运行-----");
    }

    @After
    public void after() {
        System.out.println("-----程序运行结束-----");
    }

    public Object lockA = new Object();
    public Object lockB = new Object();

    /**
     * 死循环测试
     */
    public static void main(String[] args) throws InterruptedException{
        MainThreadTest main = new MainThreadTest();
        //死循环
        executor.submit(() -> main.taskC());
        //死锁
        executor.submit(() -> main.taskA());
        executor.submit(() -> main.taskB());

        executor.shutdown();
        executor.awaitTermination(1,TimeUnit.DAYS);
    }

    public void taskC(){
        while (true){
            printM();
        }
    }

    private void printM(){
        System.out.println(RandomUtils.nextInt(100));
    }

    /**
     * 死锁测试
     */
    @Test
    public void test01() {
        executor.submit(() -> taskA());
        executor.submit(() -> taskB());
        ThreadSleepUtils.sleep(5000);
    }

    public void taskA() {
        synchronized (lockA) {
            ThreadSleepUtils.log("获取到 lockA");
            ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 500));
            synchronized (lockB) {
                ThreadSleepUtils.log("获取到 lockB");
                ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 500));
            }
        }
        ThreadSleepUtils.log("运行结束");
    }

    public void taskB() {
        synchronized (lockB) {
            ThreadSleepUtils.log("获取到 lockB");
            ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 500));
            synchronized (lockA) {
                ThreadSleepUtils.log("获取到 lockA");
                ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 500));
            }
        }
        ThreadSleepUtils.log("运行结束");
    }
}
