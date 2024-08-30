package com.go.starter.core.model.utils

import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.model.NodeModel

/**
 * 开始节点工具类
 */
class BaseNodeUtils {
    companion object {
        fun getNodeId(nodeModel: NodeModel): String {
            return nodeModel.nodeId!!
        }

        fun getNodeName(nodeModel: NodeModel): String {
            return nodeModel.nodeName!!
        }

        fun getNodeType(nodeModel: NodeModel): NodeTypeEnum {
            return nodeModel.nodeType!!
        }
    }
}

/**
 * 网关节点工具类
 */
class GatewayNodeUtils {
    companion object {
        fun getConditionsNodeIds(nodeModel: NodeModel): Set<String> {
            return nodeModel.flowConditions!!.map { it.nodeId }.toSet()
        }
    }
}