package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * 扩展流程实例表
 */
@TableName("fw_instance_ext", autoResultMap = true)
class InstanceExt {
    // 流程实例 ID
    @TableId(type = IdType.INPUT)
    var instanceId: Long? = null

    // 流程实例编号
    var instanceNo: String? = null

    // 流程定义 ID
    var processId: Long? = null

    // 流程定义 key 唯一标识
    var processKey: String? = null

    // 流程模型定义 JSON 内容
    var modelContent: String? = null
}