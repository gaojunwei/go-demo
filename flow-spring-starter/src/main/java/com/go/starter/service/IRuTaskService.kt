package com.go.starter.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.go.starter.core.FlowContext
import com.go.starter.core.model.NodeModel
import com.go.starter.domain.RuTask

/**
 * 运行时任务服务
 */
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
    fun closeTask(instanceNo: String, reason: String)

    /**
     * 统计当前实例下进行中的节点ID集合
     */
    fun listRuTaskNodeIdByInstanceId(instanceId: Long): Set<String>

    /**
     * 统计当前实例下进行中的任务
     */
    fun listRuTaskByInstanceNo(instanceNo: String): List<RuTask>

    /**
     * 查询指定用户的任务
     */
    fun pageRuTaskByAssignee(
        assignee: String,
        processKey: String? = null,
        instanceNo: String? = null,
        page: Page<RuTask>
    ): Page<RuTask>

    /**
     * 任务回退(默认删除历史任务)
     * 描述：当前进行中的任务进行回退到父级任务，删除并回滚历史任务;
     * @see com.go.starter.core.form.BaseForm.fallback 通过实现表单的 fallback 方法进行回退业务逻辑
     */
    fun backPreNodeTask(taskId: Long)

    /**
     * 当前任务回退到历史指定节点的任务
     * 删除并回滚与当前任务间的历史任务
     * @see com.go.starter.core.form.BaseForm.fallback 通过实现表单的 fallback 方法进行回退业务逻辑
     */
    fun backToPointNodeTask(taskId: Long, nodeId: String)

    /**
     * 更新任务受让人
     */
    fun updateAssignee(taskId: Long, assignee: String)

    /**
     * 更新任务候选人
     */
    fun updateCandidates(taskId: Long, candidates: List<String>)
}