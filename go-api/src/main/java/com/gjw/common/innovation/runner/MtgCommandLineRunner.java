package com.gjw.common.innovation.runner;

import com.gjw.common.innovation.service.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 服务启动后执行该操作
 */
@Slf4j
@Component
public class MtgCommandLineRunner implements CommandLineRunner, Ordered {

    @Override
    public void run(String... args) throws Exception {
        MqttPushClient.connect();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
