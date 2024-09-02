package com.go.starter.core.model

import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.exception.FlowException
import com.go.starter.core.listener.ProcessListener

/**
 * 流程定义实体
 */
class ProcessDefinition {
    /**
     * 流程定义名称
     */
    var processName: String? = null

    /**
     * 流程定义Key
     */
    var processKey: String? = null

    /**
     * 流程实例创建或结束监听器
     */
    var instanceListener: String? = null

    /**
     * 节点集合
     */
    var nodes: MutableList<NodeModel>? = null

    companion object {
        /**
         * 检测流程定义文件是否合法
         */
        fun processCheck(processDefinition: ProcessDefinition) {
            //流程定义属性校验
            FlowException.assertFalse(processDefinition.processName.isNullOrBlank(), "[流程名称]不能为空")
            FlowException.assertFalse(processDefinition.processKey.isNullOrBlank(), "[流程KEY]不能为空")
            FlowException.assertFalse(processDefinition.nodes.isNullOrEmpty(), "[流程节点]不能为空")
            processDefinition.instanceListener?.let {
                try {
                    FlowException.assertFalse(
                        !ProcessListener::class.java.isAssignableFrom(Class.forName(it)),
                        "[流程监听器]必须继承ProcessListener"
                    )
                } catch (e: ClassNotFoundException) {
                    FlowException.throwException("流程监听器必须继承ProcessListener")
                }
            }
            //开始节点校验
            val startNodes = processDefinition.nodes!!.filter { it.nodeType == NodeTypeEnum.START }
            FlowException.assertFalse(startNodes.isNullOrEmpty(), "[开始节点]不能为空")
            FlowException.assertFalse(startNodes.size != 1, "[开始节点]不能存在多个")
            //结束节点校验
            val endNodes = processDefinition.nodes!!.filter { it.nodeType == NodeTypeEnum.END }
            FlowException.assertFalse(endNodes.isNullOrEmpty(), "[结束节点]不能为空")
            //节点数据校验
            ProcessDefinition.checkNodes(processDefinition.nodes!!)
        }

        /**
         * 节点数据校验
         */
        fun checkNodes(nodes: List<NodeModel>) {
            //节点基础属性校验
            val nodeIdMap = mutableMapOf<String, Pair<Int, Int>>()
            nodes.forEach {
                it.check()
                nodeIdMap[it.nodeId!!] = Pair(0, 0)
            }
            FlowException.assertFalse(nodeIdMap.keys.size != nodes.size, "[nodeId]必须唯一")
            //节点关系校验
            nodes.forEach {
                when (it.nodeType!!) {
                    NodeTypeEnum.START, NodeTypeEnum.USER_TASK -> {
                        val targetRef = it.targetRef!!
                        val nodeId = it.nodeId!!
                        FlowException.assertFalse(
                            !nodeIdMap.keys.contains(targetRef),
                            "[targetRef:$targetRef]指向的节点不存在,nodeId:${it.nodeId}"
                        )
                        nodeIdMap[nodeId] = Pair(nodeIdMap[nodeId]!!.first, nodeIdMap[nodeId]!!.second + 1)
                        nodeIdMap[targetRef] = Pair(nodeIdMap[targetRef]!!.first + 1, nodeIdMap[targetRef]!!.second)
                    }

                    NodeTypeEnum.EXCLUSIVE_GATEWAY, NodeTypeEnum.PARALLEL_GATEWAY -> {
                        val nodeId = it.nodeId!!
                        nodeIdMap[nodeId] =
                            Pair(nodeIdMap[nodeId]!!.first, nodeIdMap[nodeId]!!.second + it.flowConditions!!.size)
                        it.flowConditions!!.map { con -> con.nodeId }.forEach { nId ->
                            FlowException.assertFalse(
                                !nodeIdMap.keys.contains(nId),
                                "[flowConditions:$nId]指向的节点不存在,nodeId:${it.nodeId}"
                            )
                            nodeIdMap[nId] = Pair(nodeIdMap[nId]!!.first + 1, nodeIdMap[nId]!!.second)
                        }
                    }

                    else -> {}
                }
            }
            //找到唯一的起始节点(0,n)
            val start = nodeIdMap.values.filter { it.first == 0 }
            FlowException.assertFalse(start.size != 1, "没有找到起始节点")
            //找到结束节点(n,0)
            val end = nodeIdMap.values.filter { it.second == 0 }
            FlowException.assertFalse(end.size != 1, "没有找到结束节点")
            //找到孤岛节点(0,0)
            val guDao = nodeIdMap.values.filter { it.first == 0 && it.second == 0 }
            FlowException.assertFalse(guDao.isNotEmpty(), "存在孤岛节点")
        }
    }
}