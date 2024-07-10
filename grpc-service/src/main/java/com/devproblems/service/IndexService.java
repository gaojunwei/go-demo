package com.devproblems.service;

import com.devproblems.cofig.SysConfig;
import com.devproblems.controller.dto.AuthorVo;
import com.devproblems.controller.dto.BookVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class IndexService {

    @Resource
    private SysConfig sysConfig;

    private static Map<String, String> mapA = new HashMap<>();
    static Map<String, String> mapB = new HashMap<>();

    static {
        mapA.put("key_a", "val_a_gjwxxxx");
        mapB.put("key_b", "val_b");
    }

    private Random random = new Random();

    public AuthorVo one(String one) {
        AuthorVo authorVo = new AuthorVo();
        authorVo.setAuthorId(101L);
        authorVo.setFirstName("java如何炼成" + (Objects.isNull(one) ? "" : one));
        authorVo.setBookVo(two());

        BookVo authorVo1 = new BookVo();
        authorVo1.setBookId(1L);
        authorVo1.setBookName("bookName 1");

        BookVo authorVo2 = new BookVo();
        authorVo2.setBookId(2L);
        authorVo2.setBookName("书名2");

        authorVo.setBookVoList(Arrays.asList(authorVo1, authorVo2));
        return authorVo;
    }

    public BookVo two() {
        BookVo authorVo = new BookVo();
        authorVo.setBookId(1L);
        authorVo.setBookName("bookName 1");
        return authorVo;
    }

    public int three() {
        int a = 0;
        int b = sysConfig.getLimit();
        int c = b / a;
        return c;
    }

    public int four() {
        int a = random.nextInt(500);
        try {
            TimeUnit.MILLISECONDS.sleep(a);
        } catch (InterruptedException e) {

        }
        return a;
    }

    public static String printLog() {
        return "静态方法被调用 " + new Random().nextInt(500);
    }

    /**
     * 消耗CPU的线程
     * 不断循环进行浮点运算
     */
    public void cpuHigh(){
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("cpu_high_thread");
            while (true){
                double pi = 0;
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    pi += Math.pow(-1, i) / (2 * i + 1);
                }
                System.out.println("Pi: " + pi * 4);
            }
        });
        thread.start();
    }

    /**
     * 内存使用过高
     * 不断新增 BigDecimal 信息到 list
     */
    public void memoryHigh() {
        new Thread(()->{
            Thread.currentThread().setName("memory_allocate_thread");
            List<BigDecimal> list = new ArrayList<>();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add(new BigDecimal(i));
            }
        }).start();
    }

    /**
     * 死锁线程
     * 线程 dead_thread_A 与 线程 dead_thread_B 互相锁死
     */
    /** 创建资源 */
    private Object resourceA = new Object();
    private Object resourceB = new Object();
    public void deadThread() {
        /** 创建资源 */
        Object resourceA = new Object();
        Object resourceB = new Object();
        // 创建线程
        Thread threadA = new Thread(() -> {
            Thread.currentThread().setName("dead_thread_A");
            synchronized (resourceA) {
                System.out.println(Thread.currentThread() + " get ResourceA");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceB");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + " get resourceB");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            Thread.currentThread().setName("dead_thread_A");
            synchronized (resourceB) {
                System.out.println(Thread.currentThread() + " get ResourceB");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceA");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread() + " get resourceA");
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}