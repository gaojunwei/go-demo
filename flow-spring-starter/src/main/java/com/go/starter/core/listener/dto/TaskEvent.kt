package com.go.starter.core.listener.dto

import com.go.starter.core.enums.TaskEventEnum

/**
 * 任务事件数据
 */
data class TaskEvent(
    // 任务ID
    val taskId: Long,
    // 流程实例 ID
    val instanceId: Long,
    // 流程实例编号
    val instanceNo: String,
    // 流程定义 KEY
    val processKey: String,
    // 任务事件
    val taskEventEnum: TaskEventEnum,
)