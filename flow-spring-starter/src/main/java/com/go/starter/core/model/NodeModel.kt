package com.go.starter.core.model

import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.enums.NodeTypeEnum.*
import com.go.starter.core.exception.FlowException

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

        when(nodeType!!){
            START,USER_TASK -> {
                FlowException.assertFalse(targetRef == null, "[targetRef]不能为空,nodeId:$nodeId")
            }
            EXCLUSIVE_GATEWAY,PARALLEL_GATEWAY -> {
                FlowException.assertFalse(flowConditions.isNullOrEmpty(), "[flowConditions]不能为空,nodeId:$nodeId")
            }
            else -> {}
        }
    }
}

/**
 * 条件实体
 */
data class FlowCondition(val nodeId:String, val conExpression:String, var result:Boolean = false)