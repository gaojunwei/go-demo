package com.jdjr.crawler.tcpj.runner;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.schedule.TCPJCatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 类描述 结束的时候执行
 *
 * @Author gaojunwei
 * @Date 2020/7/17 10:30
 **/
@Component
@Slf4j
public class MyDisposableBean implements DisposableBean {
    @Override
    public void destroy() {
        logger.info("程序定时任务执行日志记录：{}", JSON.toJSONString(TCPJCatch.getLog()));
        logger.info("缓存数据：{}", JSON.toJSONString(TCPJCatch.getCatch()));
    }
}
