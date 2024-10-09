package com.go

import jakarta.annotation.Resource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootTest // 此注解只能在 springboot 主包下使用 需包含 main 方法与 yml 配置文件
@AutoConfigureMockMvc
abstract class AbstractSpringTest {
    @Resource
    lateinit var passwordEncoder: PasswordEncoder
}
