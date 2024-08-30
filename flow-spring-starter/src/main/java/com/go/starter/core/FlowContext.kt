package com.go.starter.core

import com.alibaba.fastjson2.JSON
import com.go.starter.core.model.NodeModel
import com.go.starter.core.model.ProcessDefinition
import com.go.starter.core.utils.ProcessAnalysisUtil
import com.go.starter.domain.HisInstance

/**
 * Flow流程引擎上下文
 */
data class FlowContext(
    //流程实例信息
    var instance: HisInstance,
    //流程定义信息
    var processDefinition: ProcessDefinition,
    //流程实例全局变量
    var processVariable: Map<String, String> = mutableMapOf(),
    //父级任务ID
    var parentTaskId: Long? = null
) {

    /**
     * 获取任务节点的处理人员信息
     */
    fun getTaskUser(nodeModel: NodeModel): Pair<String, List<String>> {
        val assignee = nodeModel.assignee?.let { ProcessAnalysisUtil.eval<String>(it, processVariable) } ?: ""
        val candidateUsers = if (assignee.isBlank()) {
            nodeModel.candidateUsers?.let {
                ProcessAnalysisUtil.eval<String>(it, processVariable)
                    ?.let { item -> JSON.parseArray(item, String::class.java) } ?: emptyList()
            } ?: emptyList()
        } else {
            emptyList<String>()
        }
        return Pair(assignee, candidateUsers)
    }

    /**
     * 获取任务节点的处理人员信息
     */
    fun getTaskUser(nodeId: String): Pair<String, List<String>> {
        return getTaskUser(processDefinition.nodes!!.first { it.nodeId == nodeId })
    }

    /**
     * 获取任务节点的处理人员信息key值
     */
    fun getTaskUserKey(nodeId: String): Pair<String, String> {
        val nodeModel = ProcessAnalysisUtil.getNodeDefinition(nodeId, processDefinition)
        return Pair(
            nodeModel.assignee?.let { it.replaceFirst("#", "") } ?: "",
            nodeModel.candidateUsers?.let { it.replaceFirst("#", "") } ?: ""
        )
    }

    /**
     * 获取流程实例的监听器
     */
    fun getInstanceListener(): String? {
        return processDefinition.instanceListener
    }
}