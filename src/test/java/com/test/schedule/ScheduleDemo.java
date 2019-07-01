package com.test.schedule;

import com.alibaba.fastjson.JSON;
import com.wangyin.schedule.sdk.ScheduleSdk;
import com.wangyin.schedule.sdk.bean.NotifyType;
import com.wangyin.schedule.sdk.request.JobAddCronRequest;
import com.wangyin.schedule.sdk.request.JobDeleteRequest;
import com.wangyin.schedule.sdk.request.JobPauseRequest;
import com.wangyin.schedule.sdk.request.JobUpdateCronRequest;
import com.wangyin.schedule.sdk.response.JsonObjectResponse;
import com.wangyin.schedule.utils.JobType;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaojunwei
 * @Date: 2019/6/5 17:58
 * @Description:
 */
public class ScheduleDemo {
    private static String host = "http://172.25.4.107:80";
    private static String appId = "innovation-epl";
    private static String secret = "966c21beb93137c111ba046e3b43b5e4";

    private static String taskName = "1560406246964";

    /**
     * 添加
     */
    @Test
    public void add() throws IOException {
        JobAddCronRequest cron = new JobAddCronRequest();
        /**
         * 任务名称
         */
        cron.setDesc("测试新增CRON触发器");
        /**
         * 任务类型-分布式任务
         */
        cron.setJobType(JobType.GRID);
        /**
         * 任务类全名-格式xxx.yyy.zzz.ClassA
         */
        cron.setJobClass("com.schedule.test.ParentTaskA");
        /**
         * 定时表达式
         */
        cron.setCron("0 0/5 * * * ? *");
        /**
         * 可并发
         * 任务可并行执行
         * 不可并发
         * 任务串行执行，如果存在没执行完成的任务，再次触发的任务将被直接丢弃
         */
        cron.setConcurrency(Boolean.FALSE);
        /**
         * 事务性简介
         * 避免客户端持久化取到的任务之前因网络异常、解析任务报文失败、应用宕机、应用重启等原因导致的任务丢失问题
         * 注意事项
         * 从任务被从服务端取走的时间算起，大约需要七到八十秒的等待过程才能判定一个任务是否已丢失
         * 基于以上事实，对于触发顺序有严格要求的任务，建议触发频率在2分钟以上的启用此功能
         * 客户端版本在2.2.0及以上的，开启此选项才有效
         */
        //cron.setTransactionalAccept(Boolean.TRUE);
        /**
         * 自动中止
         * a,客户端任务执行超过预估时间,服务端自动中止此任务.对于非并发的任务和脚本任务,推荐选择自动中止
         * b,对于java实现的任务,客户端执行任务的线程会被中断,如果执行任务的线程当前是可中断的则会抛出InterruptedException,用户可以利用这个异常中止任务.
         *  如果线程是不可中断的或者不希望以InterruptedException作为中止任务的依据,
         *  客户端代码需要在一些检查点调用checkAbort()方法,抛出TaskAbortException,中止任务执行
         * c,对于脚本任务,子进程会被强制kill
         */
        cron.setAutoAbort(Boolean.TRUE);
        /**
         * 任务未取走超时时间-超过此时间，客户端无法取走此任务
         */
        cron.setExpired(300);
        /**
         * 任务执行超时时间-超过此时间，发执行超时告警
         */
        cron.setExpected(600);
        /**
         * 通知-邮箱地址或手机号
         */
        cron.setEmail(new String[]{"abc@jd.com", "15901239876"});
        /**
         * 机器分组ID
         */
        cron.setGroups(new String[]{"2856845249633028977"});
        /**
         * 任务未取走告警-通知类型
         */
        cron.addNotifyUnaccepted(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务执行超时告警-通知类型
         */
        cron.addNotifyExpired(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务执行失败告警-通知类型
         */
        cron.addNotifyFailed(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务取走未执行告警-通知类型
         */
        cron.addNotifyAccepted(NotifyType.WECHAT, NotifyType.EMAIL);

        /**
         * 设置任务参数
         */
        Map<String, String> map = new HashMap<>();
        map.put("flowNo", "12345");
        cron.setParams(map);

        ScheduleSdk sdk = new ScheduleSdk(host, appId, secret);
        sdk.setRequestRetryHandler(new DefaultHttpRequestRetryHandler());
        JsonObjectResponse r = sdk.execute(cron);
        if(r.isError()) {
            System.out.println(String.format("create cron trigger error:%s", r.getError()));
        }
        System.out.println(r.getContent().toJSONString());
    }

    /**
     * 修改
     */
    @Test
    public void update() throws IOException {
        JobUpdateCronRequest cron = new JobUpdateCronRequest();
        cron.setName(taskName);
        /**
         * 任务名称
         */
        cron.setDesc("测试新增CRON触发器修改222");
        /**
         * 任务类型-分布式任务
         */
        cron.setJobType(JobType.GRID);
        /**
         * 任务类全名-格式xxx.yyy.zzz.ClassA
         */
        cron.setJobClass("com.schedule.test.ParentTaskA");
        /**
         * 定时表达式
         */
        cron.setCron("0 0/5 * * * ? *");
        /**
         * 可并发
         * 任务可并行执行
         * 不可并发
         * 任务串行执行，如果存在没执行完成的任务，再次触发的任务将被直接丢弃
         */
        cron.setConcurrency(Boolean.FALSE);
        /**
         * 事务性简介
         * 避免客户端持久化取到的任务之前因网络异常、解析任务报文失败、应用宕机、应用重启等原因导致的任务丢失问题
         * 注意事项
         * 从任务被从服务端取走的时间算起，大约需要七到八十秒的等待过程才能判定一个任务是否已丢失
         * 基于以上事实，对于触发顺序有严格要求的任务，建议触发频率在2分钟以上的启用此功能
         * 客户端版本在2.2.0及以上的，开启此选项才有效
         */
        //cron.setTransactionalAccept(Boolean.TRUE);
        /**
         * 自动中止
         * a,客户端任务执行超过预估时间,服务端自动中止此任务.对于非并发的任务和脚本任务,推荐选择自动中止
         * b,对于java实现的任务,客户端执行任务的线程会被中断,如果执行任务的线程当前是可中断的则会抛出InterruptedException,用户可以利用这个异常中止任务.
         *  如果线程是不可中断的或者不希望以InterruptedException作为中止任务的依据,
         *  客户端代码需要在一些检查点调用checkAbort()方法,抛出TaskAbortException,中止任务执行
         * c,对于脚本任务,子进程会被强制kill
         */
        cron.setAutoAbort(Boolean.TRUE);
        /**
         * 任务未取走超时时间-超过此时间，客户端无法取走此任务
         */
        cron.setExpired(300);
        /**
         * 任务执行超时时间-超过此时间，发执行超时告警
         */
        cron.setExpected(600);
        /**
         * 通知-邮箱地址或手机号
         */
        cron.setEmail(new String[]{"abc@jd.com", "15901239876"});
        /**
         * 机器分组ID
         */
        cron.setGroups(new String[]{"2856845249633028977"});
        /**
         * 任务未取走告警-通知类型
         */
        cron.addNotifyUnaccepted(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务执行超时告警-通知类型
         */
        cron.addNotifyExpired(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务执行失败告警-通知类型
         */
        cron.addNotifyFailed(NotifyType.WECHAT, NotifyType.EMAIL);
        /**
         * 任务取走未执行告警-通知类型
         */
        cron.addNotifyAccepted(NotifyType.WECHAT, NotifyType.EMAIL);

        /**
         * 设置任务参数
         */
        Map<String, String> map = new HashMap<>();
        map.put("flowNo", "12345");
        cron.setParams(map);



        ScheduleSdk sdk = new ScheduleSdk(host, appId, secret);
        JsonObjectResponse r = sdk.execute(cron);
        if(r.isError()) {
            System.out.println(String.format("update cron trigger error:%s", r.getError()));
        }
        System.out.println("-->"+JSON.toJSONString(r));
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws IOException {
        //触发器名 JobAddCronRequest
        JobDeleteRequest req = new JobDeleteRequest(taskName);

        ScheduleSdk sdk = new ScheduleSdk(host, appId, secret);
        JsonObjectResponse r = sdk.execute(req);
        if(r.isError()) {
            System.out.println(String.format("update cron trigger error:%s", r.getError()));
        }
        System.out.println(r.getContent().toJSONString());
    }

    /**
     * 暂停
     */
    @Test
    public void suspend() throws IOException {
        //触发器名 JobAddCronRequest
        JobPauseRequest req = new JobPauseRequest(taskName);

        ScheduleSdk sdk = new ScheduleSdk(host, appId, secret);
        JsonObjectResponse r = sdk.execute(req);
        if(r.isError()) {
            System.out.println(String.format("update cron trigger error:%s", r.getError()));
        }
        System.out.println(r.getContent().toJSONString());
    }
}