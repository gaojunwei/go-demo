package com.go.starter.core.model

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
}