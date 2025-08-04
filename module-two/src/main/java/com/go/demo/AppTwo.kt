package com.go.demo

import cn.hutool.extra.spring.SpringUtil
import com.go.demo.config.SysConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class AppTwo

fun main(args: Array<String>) {
    SpringApplication(AppTwo::class.java).run(*args)
    println("******* SERVER 启动成功! VERSION:${SpringUtil.getBean(SysConfig::class.java).version}!!!")
}

