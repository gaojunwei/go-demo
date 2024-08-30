package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 历史任务表
 */
@TableName("fw_his_task", autoResultMap = true)
open class HisTask {
    // 任务 ID
    @TableId(type = IdType.INPUT)
    var taskId: Long? = null

    // 创建时间
    var startTime: LocalDateTime? = null

    // 结束时间
    var endTime: LocalDateTime? = null

    // 耗时
    var duration: Long? = null

    // 流程实例 ID
    var instanceId: Long? = null

    // 流程实例编号
    var instanceNo: String? = null

    // 流程定义 KEY
    var processKey: String? = null

    // 节点ID 唯一标识
    var nodeId: String? = null

    // 节点名称
    var nodeName: String? = null

    // 受让人
    var assignee: String? = null

    // 表单键
    var formKey: String? = null

    // 删除原因
    var deleteReason: String? = null
}