package com.jdjr.crawler.tcpj.schedule;

import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.service.TCPJService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/16 20:12
 **/
@Component
@Slf4j
public class TCPJScheduleTask {

    @Resource
    private TCPJService tcpjService;
    @Resource
    private SysConfig sysConfig;

    @Scheduled(fixedDelayString = "${tcpj.getLoginCookie.schedule:'3600000'}")
    public void getCookieTask() {
        String taskId = UuidUtils.getUUID();
        logger.info("{} task_start", taskId);
        int count = 1;
        while (true) {
            String logStr = "TaskId:%s-%s,startTime:%s,endTime:%s,耗时：%s秒,执行结果token：%s";
            Date startTime = new Date();
            //执行获取cookie任务
            String token = getTcpjCookieTask(taskId);
            Date endTime = new Date();
            try {
                token = StringUtils.isEmpty(token) ? "" : token;
                TCPJCatch.addLog(String.format(logStr, taskId, count, DateFormatUtils.dateFormat(startTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                        DateFormatUtils.dateFormat(endTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                        Long.toString((endTime.getTime() - startTime.getTime()) / 1000),
                        token));
            } catch (Exception e) {
                logger.warn("警告：日志记录异常");
            }
            if (!StringUtils.isEmpty(token)) {
                logger.info("{} task_end use {} times.", taskId, count);
                break;
            }
            count++;
        }
    }

    private String getTcpjCookieTask(String taskId) {
        String token = null;
        try {
            token = tcpjService.getLoginToken(sysConfig.getTcpjLoginPageUrl(), sysConfig.getTcpjCccount(), sysConfig.getTcpjAccountPwd());
            if (!StringUtils.isEmpty(token)) {
                TCPJCatch.applyValue(token, new Date());
                logger.info("{} Refresh catch token token:{}", taskId, token);
            } else {
                logger.info("{} TCPJScheduleTask_success {}", taskId, token);
            }
        } catch (Exception e) {
            logger.info("{} TCPJScheduleTask_exception", taskId);
        }
        return token;
    }

    @Scheduled(fixedDelayString = "60000")
    public void checkInvalid() {
        try {
            TCPJCatch.checkExpire();
        } catch (Exception e) {
            logger.error("清理过期TOKEN数据异常失败");
        }
    }
}
