package com.gjw.deme.runner;

import com.gjw.deme.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner, Ordered {

    @Resource
    private AppConfig appConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("B服务启动成功 版本：" + appConfig.getAppVersion());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
