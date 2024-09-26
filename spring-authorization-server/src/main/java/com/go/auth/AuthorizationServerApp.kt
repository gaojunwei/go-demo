package com.go.auth

import cn.hutool.extra.spring.SpringUtil
import com.go.auth.config.SysConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class AuthorizationServerApp

fun main(args: Array<String>) {
    SpringApplication(AuthorizationServerApp::class.java).run(*args)
    println("******* AuthorizationServer:${SpringUtil.getBean(SysConfig::class.java).version} 启动成功!!!")
}

