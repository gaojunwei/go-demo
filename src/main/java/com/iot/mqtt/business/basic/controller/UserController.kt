package com.iot.mqtt.business.basic.controller

import com.iot.mqtt.common.kotlin.extension.logger
import com.iot.mqtt.business.basic.controller.dto.UserInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/data")
@Tag(name = "数据中心")
class UserController {

    @Operation(summary = "查询用户")
    @Parameters(*[
        Parameter(name = "userId", description = "用户ID", required = true, `in` = ParameterIn.PATH),
        Parameter(name = "token", description = "请求token", `in` = ParameterIn.HEADER),
        Parameter(name = "userName", description = "用户名称",required = true, `in` = ParameterIn.QUERY),
    ])
    @PostMapping("/bodyParamHeaderPath/{userId}")
    fun bodyParamHeaderPath(
        @PathVariable("userId") userId: String,
        @RequestHeader(name = "token", required = false) token: String?,
        @RequestParam("userName") userName: String
    ): ResponseEntity<UserInfo> {
        logger.info("接收到参数 userId:$userId,token:$token,userName:$userName")
        return ResponseEntity.ok(UserInfo().apply {
            this.userId = 1L
            this.userName = "阿信"
        })
    }
}