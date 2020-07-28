package com.jdjr.crawler.tcpj.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 11:38
 **/
@Component
@Slf4j
public class CheckExpireTask {

    @Scheduled(fixedDelayString = "60000")
    public void checkInvalid() {
        try {
            TCPJCatch.checkExpire();
        } catch (Exception e) {
            logger.error("TCPJ 清理过期TOKEN数据异常失败");
        }
        try {
            BIHUCatch.checkExpire();
        } catch (Exception e) {
            logger.error("BIHU 清理过期TOKEN数据异常失败");
        }
    }
}
