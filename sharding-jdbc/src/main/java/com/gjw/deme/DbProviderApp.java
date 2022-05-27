package com.gjw.deme;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口
 */
@SpringBootApplication
//@ImportResource(locations = {"classpath:spring-*.xml"})
//@EnableScheduling
//@EnableTransactionManagement //开启事务管理
@MapperScan("com.jdjr.crawler.tcpj.mapper")
public class DbProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(DbProviderApp.class, args);
    }
}
