package com.gjw.deme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口
 */
@SpringBootApplication(scanBasePackages = "com.jdjr.crawler.tcpj")
@ImportResource(locations = {"classpath:spring-*.xml"})
@EnableScheduling
//@EnableTransactionManagement //开启事务管理
public class ProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }
}
