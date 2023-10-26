package com.gjw.innovation.runner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义启动运行逻辑
 * <p>
 * 方法可以看出来参数都不一样，额外科普一下Spring Boot如何传递额外参数通过命令行
 * 执行java -jar传递给main方法，规则如下键值对 格式为--K=V
 * 多个使用空格隔开值 多个空格隔开
 */
@Slf4j
@Component
public class HighOrderCommandLineRunner implements CommandLineRunner, Ordered {

    @Resource
    private Environment environment;

    @Override
    public void run(String... args) {
        log.info("项目启动");
        List<Param> paramList = Arrays.asList(
                new Param("server.tomcat.max-connections", "server.tomcat.max-connections"),
                new Param("server.tomcat.min-spare-threads", "server.tomcat.min-spare-threads"),
                new Param("server.tomcat.max-threads", "server.tomcat.max-threads"),
                new Param("server.connection-timeout", "server.connection-timeout"));
        paramList.stream().forEach(item -> {
            log.info("系统参数 Key:{},Value:{}", item.getKey(), environment.getProperty(item.getKey()));
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public class Param {
        private String key;
        private String value;
    }
}