package com.gjw.go.service;

import com.gjw.go.common.utils.UuidUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CrontabTask implements CommandLineRunner {

    @Resource
    private CommonUtil commonUtil;
    @Resource
    private RedissonClient redissonClient;

    Long a = 2L;


    @Scheduled(fixedRate = 1000)
    public void task() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        commonUtil.addAlarmNoticeDelay(UuidUtils.getUUID()+ " - "+ LocalTime.now(), 10L + a);

    }

    @Override
    public void run(String... args) {
        new Thread(() -> {
            RBlockingQueue<String> blockingDeque = redissonClient.getBlockingQueue("abc");
            while (true) {
                try {
                    log.info("******接收到延时消息 = " +  blockingDeque.take()  +" "+LocalTime.now());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
