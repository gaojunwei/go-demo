package com.go.groovy.runner;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.go.groovy.groovy.service.enums.GroovyScriptEnum;
import com.go.groovy.listener.NacosConfigChangeListener;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class InitDataRunner implements CommandLineRunner {
    @Resource
    private ConfigService configService;

    @Override
    public void run(String... args) throws Exception {
        //监控配置文件变化
        configService.addListener(GroovyScriptEnum.SPRING_METHOD_1.getDataId(), GroovyScriptEnum.SPRING_METHOD_1.getGroupId(), new NacosConfigChangeListener());
        //监控配置项变化
        afterPropertiesSet();
    }


    public void afterPropertiesSet() throws Exception {
        AbstractConfigChangeListener listener =
                new AbstractConfigChangeListener() {
                    @Override
                    public void receiveConfigChange(ConfigChangeEvent event) {
                        Collection<ConfigChangeItem> changeItems = event.getChangeItems();
                        log.info("配置项发生变化 -> {}", changeItems.toString());
                    }
                };
        configService.addListener("gjw-test.yml", "dev-group", listener);
    }
}
