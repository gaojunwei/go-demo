package com.go.starter.core.listener.dto

import com.go.starter.core.enums.InstanceEventEnum

/**
 * 流程实例事件数据
 */
data class ProcessEvent(
    // 流程实例 ID
    val instanceId: Long,
    // 流程实例编号
    val instanceNo: String,
    // 流程定义 KEY
    val processKey: String,
    // 流程事件
    val instanceEventEnum: InstanceEventEnum,
)