package com.gjw.go.common.enums

/**
 * 系统响应码
 */
enum class SysCodeEnums (var code: String,var msg: String) {
    SUCCESS("0","success"),
    ERROR("9","系统异常,请联系管理员")
}