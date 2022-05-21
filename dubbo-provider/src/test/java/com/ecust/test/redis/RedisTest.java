package com.ecust.test.redis;

import com.ecust.test.MyPolicy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jdjr.crawler.tcpj.ProviderApp;
import com.jdjr.crawler.tcpj.utils.RandomUtils;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProviderApp.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    RedisProperties.Jedis jedis;

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 100L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("gjw-task-%d").build(), new MyPolicy());

    @Test
    public void test001() throws InterruptedException {
        String lockKey = "bill001";
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            executor.submit(() -> {

                try {
                    boolean result = getLock(lockKey, "req" + finalI, 1, TimeUnit.SECONDS);
                    if (!result) {
                        return;
                    }
                    System.out.printf("%s 抢锁成功，执行业务逻辑%n", ThreadSleepUtils.ThreadName());
                    ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 500));
                } finally {
                    //释放分布式锁
                    releaseLock(lockKey, "req" + finalI);
                }
            });
        }
        ThreadSleepUtils.sleep(10000);
        System.out.println("超时后获取锁值：" + redisTemplate.opsForValue().get(lockKey));

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
    }

    /**
     * 获取分布式锁方法
     */
    public Boolean getLock(String lockKey, String reqId, long time, TimeUnit unit) {
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, reqId, time, unit))) {
            System.out.printf("%s 抢锁成功 ,lockKey:%s,reqId:%s,time:%s,unit:%s%n", ThreadSleepUtils.ThreadName(), lockKey, reqId, time, unit);
            return true;
        }
        return false;
    }

    /**
     * 释放锁的脚本
     */
    DefaultRedisScript<Long> releaseLockScript = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class);

    /**
     * 释放分布式锁
     */
    public void releaseLock(String lockKey, String reqId) {
        Optional.ofNullable(redisTemplate.execute(releaseLockScript, Collections.singletonList(lockKey), reqId)).ifPresent(flag -> {
            if (flag == 1L) {
                System.out.printf("%s 释放锁成功 lockKey:%s,reqId:%s %n", ThreadSleepUtils.ThreadName(), lockKey, reqId);
            } else {
                System.out.printf("%s 释放锁失败 lockKey:%s,reqId:%s %n", ThreadSleepUtils.ThreadName(), lockKey, reqId);
            }
        });
    }

    @Before
    public void before() {
        System.out.println("-----程序开始运行");
    }

    @After
    public void after() {
        System.out.println("-----程序运行结束");
    }
}
