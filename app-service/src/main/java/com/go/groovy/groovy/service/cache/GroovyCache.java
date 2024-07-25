package com.go.groovy.groovy.service.cache;

import com.go.groovy.exception.ServiceException;
import com.go.groovy.groovy.service.enums.GroovyScriptEnum;
import com.go.groovy.listener.MyConfigService;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class GroovyCache implements InitializingBean {
    @Resource
    private MyConfigService myConfigService;

    private final ConcurrentMap<String, Script> cache = new ConcurrentHashMap<>();

    public void reload() {
        //加锁，防止并发
        synchronized (this){
            Arrays.stream(GroovyScriptEnum.values()).forEach(item -> {
                try {
                    GroovyClassLoader classLoader = new GroovyClassLoader();
                    Class<?> clazz = classLoader.parseClass(myConfigService.getScript(item));
                    //维护缓存（）
                    cache.put(item.getKey(), (Script) clazz.newInstance());
                    //清除GroovyClassLoader的缓存
                    classLoader.clearCache();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public <T> T run(GroovyScriptEnum scriptEnum, Object[] param) {
        Script script = cache.get(scriptEnum.getKey());
        ServiceException.assertFalse(cache.get(scriptEnum.getKey()) == null, String.format("[%s]groovy脚本不存在", scriptEnum.getKey()));
        return (T) script.invokeMethod(scriptEnum.getMethodName(), param);
    }

    @Override
    public void afterPropertiesSet() {
        reload();
    }
}
