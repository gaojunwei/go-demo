package com.go.auth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SysConfig {
    @Value("\${project.version}")
    val version: String? = null
}