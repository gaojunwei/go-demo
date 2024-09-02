package com.go.starter.service

import com.go.starter.core.FlowContext
import com.go.starter.core.enums.InstanceStateEnum
import com.go.starter.core.model.NodeModel
import com.go.starter.domain.HisInstance
import com.go.starter.service.bo.CreateInstanceBo

/**
 * 流程实例服务
 * @formatter:off
 */
interface IHisInstanceService {
    /**
     * 生成流程实例
     */
    fun createInstance(process: CreateInstanceBo): Long

    /**
     * 获取实例上下文
     */
    fun flowContext(instanceNo: String): FlowContext

    /**
     * 流程实例详情
     */
    fun detailForce(instanceNo: String): HisInstance

    /**
     * 断言存在运行中的实例
     */
    fun checkExistRuInstance(instanceNo: String)

    /**
     * 修改节点受让人（修改节点生成任务实例时生效）
     */
    fun updateNodeAssignee(instanceNo: String, nodeId: String, assignee: String)

    /**
     * 修改节点候选人（修改节点生成任务实例时生效）
     */
    fun updateNodeCandidateUsers(instanceNo: String, nodeId: String, candidateUsers: List<String>)

    /**
     * 关闭流程实例
     */
    fun closeInstance(flowContext: FlowContext, instanceStateEnum: InstanceStateEnum, deleteReason: String? = null)

    /**
     * 添加节点(支持用户任务后追加同类型任务节点)，追加的节点指向节点程序自动维护
     */
    fun addUserTaskNode(instanceNo: String,afterNodeId:String,nodeModel: NodeModel)

    /**
     * 运行中实例节点受让人变更
     */
    fun changeNodeAssignee(instanceNo: String, nodeId: String, assignee: String)

    /**
     * 运行中实例节点候选人变更
     */
    fun changeNodeCandidateUsers(instanceNo: String, nodeId: String, candidateUsers: List<String>)

    /**
     * 获取可回退节点列表数据（按结束事件倒序）
     */
    fun listBackNodes(instanceNo: String): List<NodeModel>
}