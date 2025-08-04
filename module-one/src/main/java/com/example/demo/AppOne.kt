package com.example.demo

import cn.hutool.extra.spring.SpringUtil
import com.example.demo.config.SysConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class AppOne

fun main(args: Array<String>) {
    SpringApplication(AppOne::class.java).run(*args)
    println("******* SERVER 启动成功! VERSION:${SpringUtil.getBean(SysConfig::class.java).version}!!!")
}

