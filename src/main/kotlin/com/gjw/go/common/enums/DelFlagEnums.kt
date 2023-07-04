package com.gjw.go.common.enums

/**
 * 逻辑删除标识（0未删除，1已删除）
 */
enum class DelFlagEnums(value: Char, desc: String) {
    Delete('1', "已删除"),
    NotDelete('0', "未删除")
}