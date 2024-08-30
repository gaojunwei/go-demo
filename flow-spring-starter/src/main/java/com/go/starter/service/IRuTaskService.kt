package com.go.starter.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.go.starter.core.FlowContext
import com.go.starter.core.model.NodeModel
import com.go.starter.domain.RuTask

interface IRuTaskService {
    /**
     * 创建任务，并维护历史任务
     */
    fun createTask(
        flowContext: FlowContext,
        nodeModel: NodeModel,
        assignee: String = "",
        candidates: List<String> = emptyList(),
        taskVariable: Map<String, String> = emptyMap()
    ): Long

    /**
     * 领取任务
     */
    fun takeTask(
        taskId: Long,
        assignee: String
    )

    /**
     * 归还任务,重新初始化当前任务的候选人和受让人
     */
    fun giveBack(taskId: Long)

    /**
     * 完成任务
     */
    fun completeTask(
        taskId: Long,
        userId: String,
        taskVariable: Map<String, String>? = null,
        instanceVariable: Map<String, String>? = null
    )

    /**
     * 获取运行时任务详情
     */
    fun getTaskForce(taskId: Long): RuTask

    /**
     * 关闭流程任务（关闭流程实例时调用）
     */
    fun closeTask(instanceNo: String,reason: String)

    /**
     * 统计当前实例下进行中的节点ID集合
     */
    fun listRuTaskNodeIdByInstanceId(instanceId: Long): Set<String>

    /**
     * 查询指定用户的任务
     */
    fun pageRuTaskByAssignee(
        assignee: String,
        processKey: String? = null,
        instanceNo: String? = null,
        page: Page<RuTask>
    ): Page<RuTask>
}