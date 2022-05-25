package com.ecust.test.redis;

import com.ecust.test.MyPolicy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jdjr.crawler.tcpj.ProviderApp;
import com.jdjr.crawler.tcpj.controller.BloomFilterHelper;
import com.jdjr.crawler.tcpj.utils.RandomUtils;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import com.jdjr.crawler.tcpj.utils.UuidUtils;
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

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProviderApp.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private BloomFilterHelper bloomFilterHelper;

    RedisProperties.Jedis jedis;

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 100L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNameFormat("gjw-task-%d").build(), new MyPolicy());

    /**
     * 布隆过滤器实现
     */
    @Test
    public void bloom() {
        String bloomKey = "bloomKey";
        Long expectedInsertions = 1000000L;
        Double falseProbability = 0.01;//百分之一的错误率

        boolean createFlag = bloomFilterHelper.tryInitBloomFilter(bloomKey, expectedInsertions, falseProbability);
        ThreadSleepUtils.log("创建bloom过滤器结果：" + createFlag);

        boolean existFlag = bloomFilterHelper.existInBloomFilter(bloomKey, "key1");
        ThreadSleepUtils.log("检查是否存在：" + existFlag);
        bloomFilterHelper.addInBloomFilter(bloomKey, "key1");
        boolean existFlag1 = bloomFilterHelper.existInBloomFilter(bloomKey, "key1");
        ThreadSleepUtils.log("检查是否存在：" + existFlag1);

        redisTemplate.delete(bloomKey);
    }

    /**
     * 编写 redis Lua 限流脚本
     */
    DefaultRedisScript<Number> limitScript = new DefaultRedisScript<>(buildLuaScript(), Number.class);

    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c c = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append(" if c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append(" return tonumber(c)");
        lua.append(" end");
        // 执行计算器自加
        lua.append(" c = redis.call('incr',KEYS[1])");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append(" if tonumber(c) == 1 then redis.call('expire',KEYS[1],ARGV[2])");
        lua.append(" end");
        lua.append(" return c");
        return lua.toString();
    }

    /**
     * 分布式限流示例
     */
    @Test
    public void limitTest() {
        String limitKey = "bill_order";
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                if (isLimit(limitKey, 5, 1)) {
                    return;
                }
                ThreadSleepUtils.log("进行业务操作处理逻辑");
                ThreadSleepUtils.sleepMS(RandomUtils.nextInt(100, 300));
            });
        }
        ThreadSleepUtils.sleep(500);
    }

    /**
     * 判定是否触发限流
     */
    public boolean isLimit(String limitKey, Integer max, Integer second) {
        Number count = redisTemplate.execute(limitScript, Collections.singletonList(limitKey), max.toString(), second.toString());
        if (count.intValue() > max) {
            ThreadSleepUtils.log("触发限流 count:" + count.intValue() + ",limitKey:" + limitKey + ",max:" + max + ",second:" + second);
            return true;
        }
        return false;
    }

    /**
     * 分布式锁示例
     */
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
