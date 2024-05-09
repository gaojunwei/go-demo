package com.gjw.go.job.one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TaskOne {

    @Scheduled(fixedDelay = 1000)
    public void task() {
        log.info("info TaskOne 打印日志 {}", UUID.randomUUID());
        log.debug("debug TaskOne 打印日志 {}", UUID.randomUUID());
    }
}
