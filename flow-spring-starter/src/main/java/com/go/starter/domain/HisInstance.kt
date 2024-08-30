package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 历史流程实例表
 */
@TableName("fw_his_instance", autoResultMap = true)
open class HisInstance {
    // 流程实例 ID
    @TableId(type = IdType.AUTO)
    var instanceId: Long? = null

    // 流程实例编号
    var instanceNo: String? = null

    // 创建人 ID
    var createId: String? = null

    // 创建人名称
    var createBy: String? = null

    // 创建时间
    var startTime: LocalDateTime? = null

    // 结束时间
    var endTime: LocalDateTime? = null

    // 处理耗时
    var duration: Long? = null

    // 流程定义 ID
    var processId: Long? = null

    // 流程定义 key 唯一标识
    var processKey: String? = null

    // 父流程实例 ID
    var parentInstanceNo: Long? = null

    // 业务 KEY
    var businessKey: String = ""

    // 上次更新时间
    var lastUpdateTime: LocalDateTime? = null

    // 状态 0，审批中 1，审批通过 2，审批拒绝 3，撤销审批 4，超时结束 5，强制终止
    var instanceState: Byte? = null

    // 删除原因
    var deleteReason: String = ""
}