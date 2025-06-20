package com.gjw.demo.task;

import com.gjw.demo.config.SysConfig;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log
public class TaskOne {
    @Resource
    private SysConfig sysConfig;

    @Scheduled(fixedDelay = 1000)
    public void taskOne() {
        log.info("taskOne =="+sysConfig.getUserName());
    }
}
