package com.go.security

import cn.hutool.extra.spring.SpringUtil
import com.go.security.config.SysConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class SecurityApp

fun main(args: Array<String>) {
    SpringApplication(SecurityApp::class.java).run(*args)
    println("******* SecurityServer:${SpringUtil.getBean(SysConfig::class.java).version} 启动成功!!!")
}

