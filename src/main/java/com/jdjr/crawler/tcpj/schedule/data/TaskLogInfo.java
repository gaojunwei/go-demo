package com.jdjr.crawler.tcpj.schedule.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/20 14:22
 **/
@Setter
@Getter
public class TaskLogInfo {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 执行开始时间
     */
    private String startTime;
    /**
     * 执行结束时间
     */
    private String endTime;
    /**
     * 日志描述
     */
    private String desc;
    /**
     * 每个账号本次执行的日志记录
     */
    private List<AccountLogInfo> accountLogInfos;
}
