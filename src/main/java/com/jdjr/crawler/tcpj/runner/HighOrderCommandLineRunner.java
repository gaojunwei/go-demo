package com.jdjr.crawler.tcpj.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义启动运行逻辑
 * <p>
 * 方法可以看出来参数都不一样，额外科普一下 Spring Boot 如何传递额外参数通过命令行
 * 执行 java -jar 传递给 main 方法，规则如下键值对 格式为 --K=V
 * 多个使用空格隔开值 多个空格隔开
 *
 * @author gaojunwei
 * @date 2019/11/4 10:52
 */
@Slf4j
@Component
public class HighOrderCommandLineRunner implements CommandLineRunner, Ordered {

    @Value("${app.version}")
    private String appVersion;

    @Override
    public void run(String... args) {
        /**设置系统变量指定证书位置*/
        System.setProperty("javax.net.ssl.trustStore", "D:/proxy/cacerts");
        logger.info("APP_start_success version:{}", appVersion);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}