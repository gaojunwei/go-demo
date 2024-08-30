package com.go.starter.service.bo

/**
 * 流程实例启动参数
 */
data class CreateInstanceBo(
    val instanceNo: String,
    val createId: String,
    val createBy: String,
    val processKey: String,
    var parentInstanceNo: Long = 0L,
    var businessKey: String = "",
    //流程启动变量
    var processVariable:Map<String,String> = mutableMapOf()
)