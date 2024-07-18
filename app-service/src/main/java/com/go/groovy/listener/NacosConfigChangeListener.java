package com.go.groovy.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.nacos.api.config.listener.Listener;
import com.go.groovy.groovy.service.cache.GroovyCache;

import java.util.concurrent.Executor;

public class NacosConfigChangeListener implements Listener {
    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        System.out.println("监听到脚本变化 = " + configInfo);
        SpringUtil.getBean(GroovyCache.class).reload();
    }
}
