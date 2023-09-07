package org.consumer.demos.nacosdiscoveryconsumer;

import lombok.extern.slf4j.Slf4j;
import org.consumer.demos.config.SysConstant;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteServerProviderFallbackFactory implements FallbackFactory<EchoService> {

    @Override
    public EchoService create(Throwable cause) {
        log.error("[{}]服务调用失败:{}", SysConstant.ServerName.SERVICE_PROVIDER, cause.getMessage());
        return new EchoService() {
            @Override
            public String echo(String message) {
                return String.format("[%s]服务echo 方法调用失败", SysConstant.ServerName.SERVICE_PROVIDER);
            }

            @Override
            public String exception() {
                return String.format("[%s]服务exception 方法调用失败", SysConstant.ServerName.SERVICE_PROVIDER);
            }
        };
    }
}
