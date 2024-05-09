 package com.gjw.go.job.two;

 import lombok.extern.slf4j.Slf4j;
 import org.springframework.scheduling.annotation.Scheduled;
 import org.springframework.stereotype.Component;

 import java.util.UUID;

 @Component
 @Slf4j
public class TaskTwo {

     @Scheduled(fixedDelay = 1000)
     public void task() {
         log.info("info TeskTwo 打印日志 {}", UUID.randomUUID());
         log.debug("debug TeskTwo 打印日志 {}", UUID.randomUUID());
     }
}
