package com.gjw.common.innovation.service;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimedTasks {

    @Scheduled(fixedDelay = 2000)
    public void task01() {
        logger.info("任务开始执行了 {}", IdUtil.fastUUID());
    }
}
