package com.gjw.common.innovation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gaojunwei
 * @date 2019/10/14 18:34
 */
@Configuration
public class SystemConfig {
    /**
     * 跨域白名单配置
     * @return
     */
    @Bean("allowedOrigins")
    public Set<String> initOriginsSet() {
        Set<String> allowedOrigins= new HashSet<>();
        allowedOrigins.add("http://plc.jd.com");
        allowedOrigins.add("https://plc.jd.com");

        allowedOrigins.add("http://nbr.jd.com");
        allowedOrigins.add("https://nbr.jd.com");

        allowedOrigins.add("http://wu.jd.com");
        allowedOrigins.add("https://wu.jd.com");

        allowedOrigins.add("http://test.jdr.jd.com");
        allowedOrigins.add("https://test.jdr.jd.com");

        allowedOrigins.add("http://pb.jd.com");
        allowedOrigins.add("https://pb.jd.com");

        return allowedOrigins;
    }
}