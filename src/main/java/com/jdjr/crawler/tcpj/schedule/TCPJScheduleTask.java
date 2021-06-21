package com.jdjr.crawler.tcpj.schedule;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.common.util.DateUtils;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import com.jdjr.crawler.tcpj.config.SysConfig;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import com.jdjr.crawler.tcpj.schedule.data.AccountLogInfo;
import com.jdjr.crawler.tcpj.schedule.data.TaskLogInfo;
import com.jdjr.crawler.tcpj.service.TCPJService;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    @Resource
    private UserAccountService userAccountService;
    /**指定时间点不跑任务*/
    List<Integer> hours = Arrays.asList(23, 0, 1, 2, 3, 4, 5);

    /**
     * 账号登录日志模板
     * 取值依次为 账号、密码、爬取类型、执行开始时间、执行结束时间、耗时秒数、token值、重试次数
     */
    private final String logTemplete = "%s-%s-%s start:%s,end:%s,耗时：%s秒,Token值:%s,重试次数:%s";

    //@Scheduled(fixedDelayString = "${tcpj.getLoginCookie.schedule:'7000000'}")
    public void getCookieTask() {
        boolean flag = DateUtils.isNowInHour(hours);
        if (flag) {
            logger.info("tcpj 该时间点不跑任务 {}", JSON.toJSONString(hours));
            return;
        }

        //记录日志信息对象
        TaskLogInfo taskLogInfo = new TaskLogInfo();

        String taskId = UuidUtils.getUUID();
        taskLogInfo.setTaskId(taskId);
        Date start = new Date();
        taskLogInfo.setStartTime(DateFormatUtils.dateFormat(start, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss));

        logger.info("{} task_start", taskId);

        //获取可用账户
        List<UserAccount> userInfos = userAccountService.getAll(BusinessEnums.TCPJ);
        if (userInfos == null || userInfos.isEmpty()) {
            logger.error("{} tcpj not config user accounts info...", taskId);
            userAccountService.saveTaskLog(BusinessEnums.TCPJ.getValue(), String.format("taskId:%s tcpj not config user accounts info,%s", taskId, DateFormatUtils.getNowDate()));
            return;
        }
        List<AccountLogInfo> accountLogInfos = new ArrayList<>();
        try {
            /**获取登录态*/
            for (UserAccount userInfo : userInfos) {
                //记录账号登录日志
                AccountLogInfo accountLogInfo = new AccountLogInfo();
                accountLogInfo.setAccount(userInfo.getAccount());
                List<String> detailLogs = new ArrayList<>();
                String resultToken = null;
                for (int i = 1; i <= sysConfig.getTcpjAccountLoginRetryTime(); i++) {
                    Date startTime = new Date();
                    //执行获取cookie任务
                    String token = getTcpjCookieTask(taskId, userInfo);
                    Date endTime = new Date();
                    //记录本次账号登录信息日志
                    try {
                        String detailLog = String.format(logTemplete, userInfo.getAccount(), userInfo.getPassword(), userInfo.getType(),
                                DateFormatUtils.dateFormat(startTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                                DateFormatUtils.dateFormat(endTime, DateFormatUtils.FormatEnums.yyyy_MM_dd_HH_mm_ss),
                                ((endTime.getTime() - startTime.getTime()) / 1000),
                                token, i);
                        detailLogs.add(detailLog);
                    } catch (Exception e) {
                        logger.warn("警告：账号登录日志记录异常,{}-{}-{}-{}", userInfo.getAccount(), userInfo.getPassword(), userInfo.getType(), i);
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
                userAccountService.saveTaskLog(BusinessEnums.TCPJ.getValue(), JSON.toJSONString(taskLogInfo));
            } catch (Exception e) {
                logger.warn("警告：任务执行日志记录异常");
            }
            logger.info("{} task_end", taskId);
        }
    }

    /**
     * 获取指定账户号的登录Token
     */
    public String getTcpjCookieTask(String taskId, UserAccount userInfo) {
        String token = null;
        String phone = userInfo.getAccount(), password = userInfo.getPassword(), code = userInfo.getCode();
        try {
            token = tcpjService.getLoginToken(sysConfig.getTcpjLoginPageUrl(), phone, password, code);
            if (!StringUtils.isEmpty(token)) {
                //刷登录Token信息
                LoginData loginData = new LoginData();
                loginData.setToken(token);
                loginData.setSite(BusinessEnums.TCPJ.getValue());
                loginData.setAccount(userInfo.getAccount());
                loginData.setType(userInfo.getType());
                loginData.setRemark("longin:" + DateFormatUtils.getNowDate());
                userAccountService.saveToken(loginData);
                logger.info("{} Refresh catch token phone:{},token:{}", taskId, phone, token);
            } else {
                logger.info("{} phone:{},get token is null", taskId, phone);
            }
        } catch (Exception e) {
            logger.info("{} TCPJScheduleTask_exception phone:{} errorMsg:{}", taskId, phone, e.getMessage(), e);
        }
        return token;
    }
}
