package com.iot.mqtt.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SwaggerConfig {
    @Bean
    open fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("用户设备管理系统API")
                    .version("1.0")
                    .contact(Contact().apply {
                        this.name = "阿西吧"
                        this.url = "http://axiba.com"
                        this.email = "xxx.5@qq.com"
                    })
                    .description("用户设备管理系统")
                    .termsOfService("http://doc.xiaominfo.com")
                    .license(
                        License().name("Apache 2.0")
                            .url("http://doc.xiaominfo.com")
                    )
            )
    }
}