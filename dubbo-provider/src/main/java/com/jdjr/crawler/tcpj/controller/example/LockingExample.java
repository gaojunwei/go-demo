package com.jdjr.crawler.tcpj.controller.example;

import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LockingExample {
    private static final int QTY = 2;
    private static final int REPETITIONS = 2;

    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception {
        // all of the useful sample code is in ExampleClientThatLocks.java

        // FakeLimitedResource simulates some external resource that can only be access by one process at a time
        final FakeLimitedResource resource = new FakeLimitedResource();

        ExecutorService service = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            final int index = i;
            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    CuratorFramework client =  CuratorFrameworkFactory.builder()
                                    .connectString("127.0.0.1:2181")
                                    .sessionTimeoutMs(5000)
                                    .connectionTimeoutMs(5000)
                                    .retryPolicy(retryPolicy)
                                    .namespace("base")
                                    .build();
                    try {
                        client.start();

                        ExampleClientThatLocks example = new ExampleClientThatLocks(client, PATH, resource, "Client " + index);
                        for (int j = 0; j < REPETITIONS; ++j) {
                            example.doWork(3, TimeUnit.SECONDS);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // log or do something
                    } finally {
                        CloseableUtils.closeQuietly(client);
                    }
                    return null;
                }
            };
            service.submit(task);
        }
        ThreadSleepUtils.sleep(500);
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }
}
