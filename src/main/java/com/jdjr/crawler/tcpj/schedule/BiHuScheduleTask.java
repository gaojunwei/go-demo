package com.jdjr.crawler.tcpj.schedule;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.config.data.UserInfo;
import com.jdjr.crawler.tcpj.schedule.data.AccountLogInfo;
import com.jdjr.crawler.tcpj.schedule.data.TaskLogInfo;
import com.jdjr.crawler.tcpj.service.BiHuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 9:07
 **/
@Component
@Slf4j
public class BiHuScheduleTask {

    @Resource
    private BiHuService biHuService;
    @Resource
    private SysConfig sysConfig;

    /**
     * 账号登录日志模板
     * 取值依次为 账号、密码、执行开始时间、执行结束时间、耗时秒数、token值、重试次数
     */
    private final String logTemplete = "%s-%s start:%s,end:%s,耗时：%s秒,Token值:%s,重试次数:%s";

    @Scheduled(fixedDelayString = "${bihu.getLoginCookie.schedule:'14400000'}")
    public void getCookieTask() {
        //记录日志信息对象
        TaskLogInfo taskLogInfo = new TaskLogInfo();

        String taskId = UuidUtils.getUUID();
        taskLogInfo.setTaskId(taskId);
        Date start = new Date();
        taskLogInfo.setStartTime(DateFormatUtils.dateFormat(start, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss));

        logger.info("{} task_start", taskId);

        List<UserInfo> userInfos = sysConfig.getBiHuAccounts();
        if (userInfos == null || userInfos.isEmpty()) {
            logger.error("{} BIHU not config user accounts info...", taskId);
            BIHUCatch.addLog(String.format("taskId:%s BIHU not config user accounts info,%s", taskId, DateFormatUtils.getNowDate()));
            return;
        }
        List<AccountLogInfo> accountLogInfos = new ArrayList<>();

        try {
            /**获取登录态*/
            for (UserInfo userInfo : userInfos) {
                //记录账号登录日志
                AccountLogInfo accountLogInfo = new AccountLogInfo();
                accountLogInfo.setAccount(userInfo.getAccount());
                List<String> detailLogs = new ArrayList<>();
                String resultToken = null;
                for (int i = 1; i <= sysConfig.getBihuAccountLoginRetryTime(); i++) {
                    Date startTime = new Date();
                    //执行获取cookie任务
                    String token = getBiHuCookieTask(taskId, userInfo);
                    Date endTime = new Date();
                    //记录本次账号登录信息日志
                    try {
                        String detailLog = String.format(logTemplete, userInfo.getAccount(), userInfo.getPassword(),
                                DateFormatUtils.dateFormat(startTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                                DateFormatUtils.dateFormat(endTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                                ((endTime.getTime() - startTime.getTime()) / 1000),
                                token, i);
                        detailLogs.add(detailLog);
                    } catch (Exception e) {
                        logger.warn("警告：BIBU 账号登录日志记录异常,{}-{}-{}-{}", userInfo.getAccount(), userInfo.getPassword(), userInfo.getType(), i);
                    }
                    //登录成功保存Token信息
                    if (!StringUtils.isEmpty(token)) {
                        resultToken = token;
                        break;
                    }
                }
                accountLogInfo.setToken(resultToken);
                accountLogInfo.setLogs(detailLogs);
                accountLogInfos.add(accountLogInfo);
            }
        } finally {
            //记录任务执行日志
            Date end = new Date();
            taskLogInfo.setEndTime(DateFormatUtils.dateFormat(end, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss));
            taskLogInfo.setDesc(String.format("耗时:%s秒 or %s分钟", ((end.getTime() - start.getTime()) / 1000), ((end.getTime() - start.getTime()) / 60000)));
            taskLogInfo.setAccountLogInfos(accountLogInfos);
            try {
                BIHUCatch.addLog(JSON.toJSONString(taskLogInfo));
            } catch (Exception e) {
                logger.warn("警告：BIHU 任务执行日志记录异常");
            }
        }
    }

    /**
     * 获取指定账户号的登录Token
     */
    private String getBiHuCookieTask(String taskId, UserInfo userInfo) {
        String token = null;
        String phone = userInfo.getAccount(), password = userInfo.getPassword();
        try {
            token = biHuService.getLoginToken(sysConfig.getBihuLoginPageUrl(), phone, password);
            if (!StringUtils.isEmpty(token)) {
                BIHUCatch.applyValue(taskId, userInfo.getAccount(), token);
                logger.info("{} Refresh BIHU catch token phone:{},token:{}", taskId, phone, token);
            } else {
                logger.info("{} BIHU phone:{},get token is null", taskId, phone);
            }
        } catch (Exception e) {
            logger.info("{} BiHuScheduleTask_exception phone:{}", taskId, phone);
        }
        return token;
    }
}
