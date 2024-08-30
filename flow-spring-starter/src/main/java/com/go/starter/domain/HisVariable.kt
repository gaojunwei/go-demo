package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 历史变量表
 */
@TableName("fw_his_variable", autoResultMap = true)
class HisVariable {
    // 主键ID
    @TableId(type = IdType.AUTO)
    var id: Long? = null

    // 流程编号
    var instanceNo: String? = null

    // 任务ID（为0时表示为流程变量）
    var taskId: Long? = 0

    // 变量名称
    var varKey: String? = null

    // 变量值
    var varValue: String? = null

    // 创建时间
    var createTime: LocalDateTime? = null
}