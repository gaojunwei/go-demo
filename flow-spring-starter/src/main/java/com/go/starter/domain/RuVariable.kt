package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 实例运行时变量
 */
@TableName("fw_ru_variable", autoResultMap = true)
class RuVariable {
    // 任务 ID
    @TableId(type = IdType.AUTO)
    var id: Long? = null

    // 业务ID
    var instanceNo: String? = null

    // 任务 ID
    var taskId: Long? = 0

    // 变量名称
    var varKey: String? = null

    // 变量值
    var varValue: String? = null

    // 创建时间
    var createTime: LocalDateTime? = null
}