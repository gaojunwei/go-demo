package com.ecust.test.zk;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.RetryNTimes;
import org.junit.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * InterProcessReadWriteLock Demo: 分布式读写锁, 读锁为共享锁、写锁为互斥锁
 *
 * @author Aaron Zhu
 * @date 2022-03-31
 */
public class InterProcessReadWriteLockDemo1 {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private static String zkLockPath = "/Aaron/Lock2";

    private static CuratorFramework zkClient;

    @Before
    public void init() {
        // 创建客户端
        zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")    // ZK Server地址信息
                .connectionTimeoutMs(500 * 1000) // 连接超时时间: 15s
                .sessionTimeoutMs(600 * 1000)  // 会话超时时间: 60s
                // 重试策略: 重试3次, 每次间隔1s
                .retryPolicy(new RetryNTimes(3, 1000))
                .namespace("base")
                .build();
        // 启动客户端
        zkClient.start();
        System.out.println("---------------------- 系统上线 ----------------------");
    }

    /**
     * 测试: 读锁为共享锁
     */
    @Test
    public void test1Read() {
        System.out.println("\n---------------------- Test 1 : Read ----------------------");
        for (int i = 1; i <= 3; i++) {
            String taskName = "读任务#" + i;
            Runnable task = new ReadTask(taskName, zkClient, zkLockPath);
            threadPool.execute(task);
        }
        // 主线程等待所有任务执行完毕
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {

        }
    }

    /**
     * 测试: 写锁为互斥锁
     */
    @Test
    public void test2Write() {
        System.out.println("\n---------------------- Test 2 : Write ----------------------");
        //for (int i = 1; i <= 2; i++) {
            String taskName = "写任务#1" ;
            Runnable task = new WriteTask(taskName, zkClient, zkLockPath);
            threadPool.execute(task);
        //}
        // 主线程等待所有任务执行完毕
        try {
            Thread.sleep(30 * 1000);
        } catch (Exception e) {
        }
    }

    /**
     * 测试: 读写互斥
     */
    @Test
    public void test2ReadWrite() {
        System.out.println("\n---------------------- Test 3 : Read Write ----------------------");
        //for (int i = 1; i <= 8; i++) {
            Runnable task = null;
            threadPool.execute(new ReadTask("读任务#1", zkClient, zkLockPath));
            //threadPool.execute(new WriteTask("写任务#1", zkClient, zkLockPath));
        //}
        // 主线程等待所有任务执行完毕
        try {
            Thread.sleep(40 * 1000);
        } catch (Exception e) {
        }
    }

    @After
    public void close() {
        // 关闭客户端
        zkClient.close();
        System.out.println("---------------------- 系统下线 ----------------------");
    }

    /**
     * 打印信息
     *
     * @param msg
     */
    private static void info(String msg) {
        String time = formatter.format(LocalTime.now());
        String thread = Thread.currentThread().getName();
        String log = "[" + time + "] " + " <" + thread + "> " + msg;
        System.out.println(log);
    }

    /**
     * 读任务
     */
    private static class ReadTask implements Runnable {
        private String taskName;

        private InterProcessReadWriteLock.ReadLock readLock;

        public ReadTask(String taskName, CuratorFramework zkClient, String zkLockPath) {
            this.taskName = taskName;
            InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(zkClient, zkLockPath);
            this.readLock = interProcessReadWriteLock.readLock();
        }

        @SneakyThrows
        @Override
        public void run() {
            readLock.acquire();
            info(taskName + ": 成功获取读锁 #1");
            try {
                // 模拟业务耗时
                Thread.sleep(RandomUtils.nextLong());
            } catch (Exception e) {
                System.out.println(taskName + ": Happen Exception: " + e.getMessage());
            } finally {
                info(taskName + ": 释放读锁 #1");
                try {
                    readLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写任务
     */
    private static class WriteTask implements Runnable {
        private String taskName;

        private InterProcessReadWriteLock.WriteLock writeLock;

        public WriteTask(String taskName, CuratorFramework zkClient, String zkLockPath) {
            this.taskName = taskName;
            InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(zkClient, zkLockPath);
            this.writeLock = interProcessReadWriteLock.writeLock();
        }

        @Override
        public void run() {
            try {
                writeLock.acquire();
                info(taskName + ": 成功获取写锁 #1");
                // 模拟业务耗时
                Thread.sleep(RandomUtils.nextLong());
            } catch (Exception e) {
                System.out.println(taskName + ": Happen Exception: " + e.getMessage());
            } finally {
                info(taskName + ": 释放写锁 #1\n");
                try {
                    writeLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}