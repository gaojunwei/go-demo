package com.iot.mqtt.business.basic.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "UserInfo-用户信息")
class UserInfo {
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    var userId:Long? = null
    @Schema(description = "用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    var userName:String? = null
}