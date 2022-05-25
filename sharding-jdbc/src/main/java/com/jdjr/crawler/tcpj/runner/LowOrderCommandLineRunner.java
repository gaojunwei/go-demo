package com.jdjr.crawler.tcpj.runner;

import com.jdjr.crawler.tcpj.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class LowOrderCommandLineRunner implements CommandLineRunner, Ordered {
    @Resource
    private AppConfig appConfig;

    @Override
    public void run(String... args) {
        logger.info("A服务启动成功 版本：" + appConfig.getAppVersion());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
