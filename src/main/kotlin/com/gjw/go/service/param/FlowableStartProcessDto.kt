package com.gjw.go.service.param

/**
 * 启动流程参数
 */
class FlowableStartProcessDto {
    /**
     * 流程定义编码
     */
    var processDefinitionKey: String? = null

    /**
     * 变量
     */
    var variables: Map<String, Any>? = null

    /**
     * 业务唯一标识
     */
    var businessKey: String? = null

    /**
     * 当前用户ID
     */
    var currentUserId: String? = null
}