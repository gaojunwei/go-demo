package com.jdjr.crawler.tcpj.schedule;

import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.service.TCPJHitService;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 11:38
 **/
@Component
@Slf4j
public class CheckExpireTask {

    @Resource
    private UserAccountService userAccountService;
    @Resource
    private TCPJHitService tcpjHitService;

    /**
     * 清理过期Token任务
     */
    @Scheduled(fixedDelayString = "60000")
    public void checkInvalid() {
        try {
            long tcpjGQ = 6 * 3600 * 1000;
            Date timePoint = new Date(System.currentTimeMillis() - tcpjGQ);
            userAccountService.clearExpiredData(BusinessEnums.TCPJ, timePoint);
        } catch (Exception e) {
            logger.error("TCPJ 清理过期TOKEN数据异常失败");
        }
        try {
            long bihuGQ = 12 * 3600 * 1000;
            Date timePoint = new Date(System.currentTimeMillis() - bihuGQ);
            userAccountService.clearExpiredData(BusinessEnums.BIHU, timePoint);
        } catch (Exception e) {
            logger.error("BIHU 清理过期TOKEN数据异常失败");
        }
    }

    /**
     * 定期检测同城账号 是否命中风控信息
     */
    @Scheduled(fixedDelayString = "3600000")
    public void checkHitRisk() {
        String taskId = UuidUtils.getUUID();
        logger.info("{} checkHitRisk task start...",taskId);
        try {
            tcpjHitService.checkHit(taskId);
        } catch (Exception e) {
            logger.error("checkHitRiskTask 执行异常 {}", e.getMessage(), e);
        }
        logger.info("{}checkHitRisk task end",taskId);
    }
}
