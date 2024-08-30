package com.go.starter.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler
import java.time.LocalDateTime

/**
 * 待处理的任务表
 */
@TableName("fw_ru_task", autoResultMap = true)
class RuTask {
    // 任务 ID
    @TableId(type = IdType.AUTO)
    var taskId: Long? = null

    // 流程实例 ID
    var instanceId: Long? = null

    // 流程实例编号
    var instanceNo: String? = null

    // 流程定义 KEY
    var processKey: String? = null

    // 节点名称
    var nodeName: String? = null

    // 节点 key 唯一标识
    var nodeId: String? = null

    // 受让人
    var assignee: String? = null

    // 候选人集合
    @TableField(typeHandler = Fastjson2TypeHandler::class)
    var candidates: List<String> = emptyList()

    // 表单键
    var formKey: String? = null

    // 创建时间
    var createTime: LocalDateTime? = null

    //父级任务ID
    var parentTaskId: Long? = null
}