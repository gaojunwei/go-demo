package com.go.starter.core.model

import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.enums.NodeTypeEnum.*
import com.go.starter.core.exception.FlowException
import com.go.starter.core.listener.TaskListener

/**
 * 节点实例
 */
class NodeModel {
    /**
     * 节点ID-不能为空
     */
    var nodeId: String? = null

    /**
     * 节点名称-不能为空
     */
    var nodeName: String? = null

    /**
     * 节点类型-不能为空
     */
    var nodeType: NodeTypeEnum? = null

    /**
     * 条件表达式(网关)
     */
    var flowConditions: List<FlowCondition>? = null

    /**
     * 受让人，节点类型为USER_TASK此值有效
     */
    var assignee: String? = null

    /**
     * 候选人列表，节点类型为USER_TASK此值有效
     */
    var candidateUsers: String? = null

    /**
     * 表单key，节点类型为USER_TASK此值有效
     */
    var formKey: String? = null

    /**
     * 任务创建监听器，节点类型为USER_TASK此值有效
     */
    var taskListener: String? = null

    /**
     * 指向节点，节点类型为START及USER_TASK此值不能为空
     */
    var targetRef: String? = null

    /**
     * 节点数据校验
     */
    fun check() {
        FlowException.assertFalse(nodeId.isNullOrBlank(), "[nodeId]不能为空,nodeId:$nodeId")
        FlowException.assertFalse(nodeName.isNullOrBlank(), "[nodeName]不能为空,nodeId:$nodeId")
        FlowException.assertFalse(nodeType == null, "[nodeType]不能为空,nodeId:$nodeId")

        when (nodeType!!) {
            START -> {
                FlowException.assertFalse(targetRef.isNullOrBlank(), "[targetRef]不能为空,nodeId:$nodeId")
            }

            USER_TASK -> {
                FlowException.assertFalse(targetRef.isNullOrBlank(), "[targetRef]不能为空,nodeId:$nodeId")
                FlowException.assertFalse(assignee.isNullOrBlank(), "[assignee]不能为空,nodeId:$nodeId")
                FlowException.assertFalse(candidateUsers.isNullOrBlank(), "[candidateUsers]不能为空,nodeId:$nodeId")
                this.taskListener?.let {
                    try {
                        FlowException.assertFalse(
                            !TaskListener::class.java.isAssignableFrom(Class.forName(it)),
                            "[任务监听器]必须继承TaskListener"
                        )
                    } catch (e: ClassNotFoundException) {
                        FlowException.throwException("任务监听器必须继承 TaskListener")
                    }
                }
            }

            EXCLUSIVE_GATEWAY, PARALLEL_GATEWAY -> {
                FlowException.assertFalse(flowConditions.isNullOrEmpty(), "[flowConditions]不能为空,nodeId:$nodeId")
            }

            else -> {}
        }
    }

    companion object {
        /**
         * 构建用户任务节点
         */
        fun ofUserTaskNodeModel(
            nodeId: String,
            nodeName: String,
            targetRef: String,
            assignee: String,
            candidateUsers: String,
            formKey: String? = null,
            taskListener: String? = null
        ): NodeModel {
            return NodeModel().apply {
                this.nodeId = nodeId
                this.nodeName = nodeName
                this.nodeType = USER_TASK
                this.targetRef = targetRef
                this.assignee = assignee
                this.candidateUsers = candidateUsers
                this.formKey = formKey
                this.taskListener = taskListener
            }
        }
    }
}

/**
 * 条件实体
 */
data class FlowCondition(val nodeId: String, val conExpression: String, var result: Boolean = false)