package com.gjw.go.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class CommonUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发布延时消息
     */
    public void addAlarmNoticeDelay(String msg, Long delay) {
        RBlockingQueue<String> blockingDeque = redissonClient.getBlockingQueue("abc");
        //RBlockingQueue<String> blockingDeque = redissonClient.getBlockingDeque("abc");//java.lang.NoClassDefFoundError: java/util/SequencedCollection
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(msg, delay, TimeUnit.SECONDS);
        log.info("发布延时消息：msg:{}", msg);
    }
}
