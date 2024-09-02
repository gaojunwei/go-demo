package com.go.starter.core.utils

import com.alibaba.fastjson2.JSON
import com.go.starter.core.FlowContext
import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.exception.FlowException
import com.go.starter.core.model.NodeModel
import com.go.starter.core.model.ProcessDefinition
import com.go.starter.core.model.utils.BaseNodeUtils
import com.go.starter.core.model.utils.GatewayNodeUtils
import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * 流程处理工具
 * @formatter:off
 */
class ProcessAnalysisUtil {
    companion object {
        // spel表达式解析器
        val parser = SpelExpressionParser()

        /**
         * 获取指定节点定义信息
         */
        fun getNodeDefinition(nodeId: String? = null, processDefinition: ProcessDefinition): NodeModel {
            val nodeList = processDefinition.nodes!!
            return if (nodeId == null) {
                nodeList.first { it.nodeType!! == NodeTypeEnum.START }
            } else {
                processDefinition.nodes!!.first { it.nodeId == nodeId }
            }
        }


        /**
         * 获取下一个可执行节点
         */
        fun nextNode(
            nextNodeId: String,
            flowContext: FlowContext,
            ruTaskNodeIds: Set<String>,
        ): List<NodeModel> {
            val processDefinition = flowContext.processDefinition
            val nodeList = processDefinition.nodes!!
            // 获取节点信息
            val nextNode = if (ruTaskNodeIds.isEmpty() || determineParallelTaskContinue(nextNodeId, ruTaskNodeIds, processDefinition)) {
                nodeList.first { it.nodeId == nextNodeId }
            } else {
                null
            }
            if (nextNode == null) return emptyList()
            when (nextNode.nodeType!!) {
                //用户任务
                NodeTypeEnum.USER_TASK -> {
                    return mutableListOf(nextNode)
                }
                //排他网关
                NodeTypeEnum.EXCLUSIVE_GATEWAY -> {
                    val nodeId = analysisExclusiveGatewayNode(nextNode, flowContext)
                    return nextNode(nodeId, flowContext, ruTaskNodeIds)
                }
                //并行网关
                NodeTypeEnum.PARALLEL_GATEWAY -> {
                    val nodeIdSet = analysisParallelGatewayNode(nextNode, flowContext)
                    val nodeModels = nodeIdSet.map { getNodeDefinition(it, processDefinition) }
                    val userTaskList = nodeModels.filter { it.nodeType == NodeTypeEnum.USER_TASK }
                    val otherTaskList = nodeModels.filter { it.nodeType != NodeTypeEnum.USER_TASK }
                    val otherNodeModels = otherTaskList.map { node ->
                        nextNode(node.nodeId!!, flowContext, ruTaskNodeIds)
                    }.flatten()

                    return userTaskList + otherNodeModels
                }

                else -> {
                    return mutableListOf(nextNode)
                }
            }
        }

        /**
         * 判断并行任务下一个节点是否继续
         */
        private fun determineParallelTaskContinue(
            nextNodeId: String,
            ruTaskNodeIds: Set<String>,
            processDefinition: ProcessDefinition
        ): Boolean {
            val analysedNode = mutableSetOf<String>()
            ruTaskNodeIds.forEach { ruTaskNodeId ->
                if(checkInNextNode(ruTaskNodeId,processDefinition,analysedNode,nextNodeId)){
                    return false
                }
            }
            return true
        }

        /**
         * 判断指定节点的所有后续节点是否含有 将要执行的节点，如果有返回true，否则返回false
         */
        private fun checkInNextNode(nodeId: String,processDefinition: ProcessDefinition,analysedNode:MutableSet<String>,nextNodeId: String):Boolean{
            val nodeModel = getNodeDefinition(nodeId,processDefinition)
            while (true){
                val nextNodeModel = getNodeDefinition(nodeModel.targetRef!!,processDefinition)
                if(nextNodeId == BaseNodeUtils.getNodeId(nextNodeModel)){
                    return true
                }
                //添加到已经解析过的节点集合中
                analysedNode.add(BaseNodeUtils.getNodeId(nextNodeModel))
                when(nextNodeModel.nodeType!!){
                    NodeTypeEnum.USER_TASK -> {
                        return checkInNextNode(BaseNodeUtils.getNodeId(nextNodeModel),processDefinition,analysedNode,nextNodeId)
                    }
                    NodeTypeEnum.EXCLUSIVE_GATEWAY,NodeTypeEnum.PARALLEL_GATEWAY -> {
                        val nodeIds = GatewayNodeUtils.getConditionsNodeIds(nextNodeModel)
                        if(nodeIds.contains(nextNodeId)){
                            return true
                        }
                        analysedNode.addAll(nodeIds)
                        var nodeModels = nodeIds.map { getNodeDefinition(it,processDefinition) }.toMutableSet()
                        val otherNodeIds = nodeModels.filter { it.nodeType != NodeTypeEnum.EXCLUSIVE_GATEWAY && it.nodeType != NodeTypeEnum.PARALLEL_GATEWAY }.map { it.nodeId!! }.toMutableSet()
                        val gatewayNodes = nodeModels.filter { it.nodeType == NodeTypeEnum.EXCLUSIVE_GATEWAY || it.nodeType == NodeTypeEnum.PARALLEL_GATEWAY }.map { it.nodeId!! }.toMutableSet()
                        while (gatewayNodes.isNotEmpty()){
                            gatewayNodes.forEach { gatewayNodeId ->
                                val nodeIdSet = GatewayNodeUtils.getConditionsNodeIds(getNodeDefinition(gatewayNodeId,processDefinition))
                                if(nodeIdSet.contains(nextNodeId)){
                                    return true
                                }
                                //添加到已解析过节点集合
                                analysedNode.addAll(nodeIdSet)
                                nodeModels = nodeIdSet.map { getNodeDefinition(it,processDefinition) }.toMutableSet()
                                otherNodeIds.addAll(nodeModels.filter { it.nodeType != NodeTypeEnum.EXCLUSIVE_GATEWAY && it.nodeType != NodeTypeEnum.PARALLEL_GATEWAY }.map { it.nodeId!! }.toSet())
                                gatewayNodes.addAll(nodeModels.filter { it.nodeType == NodeTypeEnum.EXCLUSIVE_GATEWAY || it.nodeType == NodeTypeEnum.PARALLEL_GATEWAY }.map { it.nodeId!! }.toSet())
                                //移除已解析的节点
                                gatewayNodes.remove(gatewayNodeId)
                            }
                        }
                        otherNodeIds.forEach { nodeId ->
                            return checkInNextNode(nodeId,processDefinition,analysedNode,nextNodeId)
                        }
                    }
                    NodeTypeEnum.END -> {
                        return false
                    }
                    NodeTypeEnum.START -> {
                        FlowException.throwException("流程定义文件不合法")
                    }
                }
            }
        }

        /**
         * json格式流程定义文件转实体类
         */
        fun processModelToProcessDefinition(processModel: String): ProcessDefinition {
            return JSON.parseObject(processModel, ProcessDefinition::class.java)
        }

        /**
         * 执行spel表达式
         */
        inline fun <reified T : Any> eval(spelStr: String, args: Map<String, Any>): T? {
            val context: EvaluationContext = StandardEvaluationContext()
            for ((key, value) in args) {
                context.setVariable(key, value)
            }
            return parser.parseExpression(spelStr).getValue(context, T::class.java)
        }

        /**
         * 解析排他网关节点，并返回下一个节点ID
         */
        private fun analysisExclusiveGatewayNode(
            node: NodeModel,
            flowContext: FlowContext
        ): String {
            node.flowConditions!!.forEach {
                it.result = eval(it.conExpression, flowContext.processVariable)!!
                if (it.result) return@forEach
            }
            return node.flowConditions!!.first { it.result }.nodeId
        }

        /**
         * 解析排他网关节点，并返回下一个节点ID
         */
        private fun analysisParallelGatewayNode(
            node: NodeModel,
            flowContext: FlowContext
        ): Set<String> {
            node.flowConditions!!.forEach {
                it.result = eval(it.conExpression, flowContext.processVariable)!!
            }
            return node.flowConditions!!.filter { it.result }.map { it.nodeId }.toSet()
        }
    }
}