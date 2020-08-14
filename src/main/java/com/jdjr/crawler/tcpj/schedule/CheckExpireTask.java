package com.jdjr.crawler.tcpj.schedule;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.result.BasicResult;
import com.jdjr.crawler.tcpj.common.util.DateUtils;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import com.jdjr.crawler.tcpj.service.TCPJHitService;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    @Resource
    private TCPJScheduleTask tcpjScheduleTask;

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

    /**指定时间点不跑任务*/
    List<Integer> hours = Arrays.asList(23, 0, 1, 2, 3, 4, 5);

    /**
     * 定期检测同城账号 是否命中风控信息，或Token过期
     */
    @Scheduled(fixedDelayString = "600000")
    public void checkHitRisk() {
        boolean flag = DateUtils.isNowInHour(hours);
        if (flag) {
            logger.info("tcpj checkHitRisk 该时间点不跑任务 {}", JSON.toJSONString(hours));
            return;
        }
        String taskId = UuidUtils.getUUID();
        logger.info("{} checkHitRisk task start...", taskId);
        try {
            tcpjHitService.checkHit(taskId);
        } catch (Exception e) {
            logger.error("checkHitRiskTask 执行异常 {}", e.getMessage(), e);
        }
        logger.info("{} checkHitRisk task end", taskId);
    }

    /**
     * 恢复失效的Token(每分钟执行一次)
     */
    @Scheduled(fixedDelayString = "60000")
    public void recoveryExpiredToken() {
        String taskId = UuidUtils.getUUID();
        logger.info("{} 恢复失效的Token task start...", taskId);
        try {
            //获取过期的同城账号信息
            UserAccount userAccount = new UserAccount();
            userAccount.setSite(BusinessEnums.TCPJ.getValue());
            userAccount.setCode("401");
            List<UserAccount> userAccounts = userAccountService.listUserByCon(userAccount);
            if (userAccounts == null || userAccounts.isEmpty()) {
                logger.info("无Token过期账号");
                return;
            }
            //将过期账号重新模拟登陆获取最新的Token
            for (UserAccount account : userAccounts) {
                int retryTimes = 3;
                for (int i = 1; i <= retryTimes; i++) {
                    String token = tcpjScheduleTask.getTcpjCookieTask(taskId, account);
                    if (!StringUtils.isEmpty(token)) {
                        BasicResult result = tcpjHitService.checkFP(taskId, account.getAccount(), account.getSite(), token);
                        logger.info("{} 重新登录完，检测命中或过期 {},{} {}", taskId,account.getSite(),account.getAccount(), JSON.toJSONString(result));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("{} 恢复失效的Token 执行异常 {}", taskId, e.getMessage(), e);
        } finally {
            logger.info("{} 恢复失效的Token task end", taskId);
        }
    }
}
