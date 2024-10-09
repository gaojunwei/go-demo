package com.go

import cn.hutool.extra.spring.SpringUtil
import com.go.config.SysConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class Oauth2SecurityApp

fun main(args: Array<String>) {
    SpringApplication(Oauth2SecurityApp::class.java).run(*args)
    println("******* SecurityServer:${SpringUtil.getBean(SysConfig::class.java).version} 启动成功!!!")
}

