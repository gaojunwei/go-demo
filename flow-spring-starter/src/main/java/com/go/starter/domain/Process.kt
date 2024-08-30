package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("fw_process", autoResultMap = true)
open class Process {
    // 主键 ID
    @TableId(type = IdType.AUTO)
    var processId: Long? = null

    // 流程定义 key 唯一标识
    var processKey: String? = null

    // 流程定义名称
    var processName: String? = null

    // 流程状态 0，不可用 1，可用
    var processState: Boolean? = true

    // 流程模型定义 JSON 内容
    var modelContent: String? = null

    // 创建时间
    var createTime: LocalDateTime? = null
}